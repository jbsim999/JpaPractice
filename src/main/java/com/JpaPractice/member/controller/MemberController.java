package com.JpaPractice.member.controller;

import com.JpaPractice.dto.MultiResponseDto;
import com.JpaPractice.dto.SingleResponseDto;
import com.JpaPractice.member.dto.MemberDto;
import com.JpaPractice.member.entity.Member;
import com.JpaPractice.member.mapper.MemberMapper;
import com.JpaPractice.member.service.MemberService;
import com.JpaPractice.stamp.Stamp;
import com.JpaPractice.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v0/members")
@Validated
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/v0/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody){
        //TODO post 리퀘스트(email,name) -> 리스폰스
        Member member = mapper.memberPostToMember(requestBody);
        member.setStamp(new Stamp());

        Member createdMember = memberService.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id")@Positive long memberId,
                                      @Valid @RequestBody MemberDto.Patch requestBody){
        //TODO patch 리퀘스트(memberId, 바꿀 정보) -> 정보 조회 -> 리스폰스
        requestBody.setMemberId(memberId);

        Member member =
                memberService.updateMember(mapper.memberPatchToMember(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(member)),
                HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id")@Positive long memberId){
        //TODO get 리퀘스트(memberId) -> 리스폰스
        Member member = memberService.findMember(memberId);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.memberToMemberResponse(member)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size){
        //TODO getAll 리퀘스트 -> 리스폰스(페이지네이션 필요)
        Page<Member> pageMembers = memberService.findMembers(page -1, size);
        List<Member> members = pageMembers.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.membersToMemberResponse(members),pageMembers)
                ,HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id")@Positive long memberId){
        //TODO delete 리퀘스트(memberId) -> 리스폰스(서비스단에서 delete 구현)
        memberService.deleteMember(memberId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

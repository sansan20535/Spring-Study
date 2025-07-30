package com.jpa.entity.default_column.controller;

import com.jpa.entity.default_column.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/default-column")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public String addMembers() {
        memberService.addMembers();
        return "Created";
    }
}

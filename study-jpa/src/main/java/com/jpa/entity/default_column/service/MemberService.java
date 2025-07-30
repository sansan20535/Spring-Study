package com.jpa.entity.default_column.service;

import com.jpa.entity.default_column.entity.Member;
import com.jpa.entity.default_column.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void addMembers() {
        Integer test1 = 1;
        Integer test2 = 2;
        Integer test3 = 3;

        memberRepository.save(
                Member.builder()
                        .test1(test1)
                        .test2(test2)
                        .test3(test3)
                        .build()
        );

        memberRepository.save(
                Member.builder()
                        .test2(test2)
                        .test3(test3)
                        .build()
        );

        memberRepository.save(
                Member.builder()
                        .test1(test1)
                        .test3(test3)
                        .build()
        );
    }

}

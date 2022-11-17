package com.guro.kokeetea_project.service;

import com.guro.kokeetea_project.constant.Role;
import com.guro.kokeetea_project.dto.MemberFormDto;
import com.guro.kokeetea_project.entity.Member;
import com.guro.kokeetea_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(MemberFormDto memberFormDto){
        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        if (memberFormDto.getEmail().equals("kokeetea@gmail.com")) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.USER);
        }
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public Boolean validateAdmin(String email){
        Member findMember = memberRepository.findByEmail(email);
        return findMember != null && findMember.getRole() == Role.ADMIN;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
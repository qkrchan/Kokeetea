package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.MemberFormDto;
import com.guro.kokeetea_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String newMember(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/register";
    }

    @PostMapping(value = "/new")
    public String newMemberPost(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/register";
        }

        try {
            memberFormDto.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
            memberService.saveMember(memberFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/register";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }
}
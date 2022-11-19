package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.StatisticsDTO;
import com.guro.kokeetea_project.service.MainService;
import com.guro.kokeetea_project.service.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RequestService requestService;

    private final MainService mainService;

    @GetMapping(value="/")
    public String main(Optional<Integer> page, Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return "redirect:/member/login";
        }
        try {

        } catch (Exception e) {
            model.addAttribute("stat", new StatisticsDTO());
        }
        model.addAttribute("username", principal.getName());

        return "main";
    }

    @GetMapping(value="/ingredindAvg")
    public @ResponseBody ResponseEntity ingredindAvg() {
        List<Integer> mainAvgList;
        try {
            mainAvgList = mainService.readRequest();
            for (Integer mainAvg : mainAvgList) {
                System.out.println(mainAvg.toString());
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Integer>>(mainAvgList, HttpStatus.OK);
    }

    @GetMapping(value="/ingredindCount")
    public @ResponseBody ResponseEntity ingredindCount() {
        List<Integer> mainAvgList;
        try {
            mainAvgList = mainService.ingredindCount();
            for (Integer mainAvg : mainAvgList) {
                System.out.println(mainAvg.toString());
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Integer>>(mainAvgList, HttpStatus.OK);
    }

    @GetMapping(value="/monthCount")
    public @ResponseBody ResponseEntity monthCount() {
        Object month;
        try {
            month = mainService.monthCount();
            System.out.println(month.toString());
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(month, HttpStatus.OK);
    }

    @GetMapping(value="/storeCount")
    public @ResponseBody ResponseEntity storeCount() {
        System.out.println("storeCount================");
        List<Integer> storeCount;
        try {
            storeCount = mainService.storeCount();
            System.out.println(storeCount.toString());
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Integer>>(storeCount, HttpStatus.OK);
    }
}
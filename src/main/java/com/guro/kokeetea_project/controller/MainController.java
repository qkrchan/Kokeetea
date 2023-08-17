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
        float total = 0;
        float avg_70 = 0;
        float avg_71 = 0;
        float avg_72 = 0;
        float avg_73 = 0;
        float avg_74 = 0;
        float avg_75 = 0;
        float avg_76 = 0;
        float avg_77 = 0;
        float avg_78 = 0;
        float avg_79 = 0;
        List<Integer> mainAvgList = null;
        try {
            mainAvgList = mainService.storeCount();

            model.addAttribute("stat", requestService.stat());
        } catch (Exception e) {
            model.addAttribute("stat", new StatisticsDTO());
        }
        avg_70 = mainAvgList.get(0);
//        avg_71 = mainAvgList.get(1);
//        avg_72 = mainAvgList.get(2);
//        avg_73 = mainAvgList.get(3);
//        avg_74 = mainAvgList.get(4);
//        avg_75 = mainAvgList.get(5);
//        avg_76 = mainAvgList.get(6);
//        avg_77 = mainAvgList.get(7);
//        avg_78 = mainAvgList.get(8);
//        avg_79 = mainAvgList.get(9);

        total = avg_70 + avg_71 +avg_72 + avg_73;



        avg_70 = (avg_70/total)*100;
        avg_71 = (avg_71/total)*100;
        avg_72 = (avg_72/total)*100;
        avg_73 = (avg_73/total)*100;
        avg_74 = (avg_74/total)*100;
        avg_75 = (avg_75/total)*100;
        avg_76 = (avg_76/total)*100;
        avg_77 = (avg_77/total)*100;
        avg_78 = (avg_78/total)*100;
        avg_79 = (avg_79/total)*100;
        System.out.println(total);
        System.out.println(avg_70);
        System.out.println(avg_71);
        System.out.println(avg_72);
        System.out.println(avg_73);
        model.addAttribute("username", principal.getName());
        model.addAttribute("avg_70", String.format("%.2f", avg_70));
        model.addAttribute("avg_71", String.format("%.2f", avg_71));
        model.addAttribute("avg_72", String.format("%.2f", avg_72));
        model.addAttribute("avg_73", String.format("%.2f", avg_73));
        model.addAttribute("avg_74", String.format("%.2f", avg_74));
        model.addAttribute("avg_75", String.format("%.2f", avg_75));
        model.addAttribute("avg_76", String.format("%.2f", avg_76));
        model.addAttribute("avg_77", String.format("%.2f", avg_77));
        model.addAttribute("avg_78", String.format("%.2f", avg_78));
        model.addAttribute("avg_79", String.format("%.2f", avg_79));

        return "main";
    }

    @GetMapping(value="/ingredindAvg")
    public @ResponseBody ResponseEntity ingredindAvg() {
        List<Integer> mainAvgList;
        try {
            mainAvgList = mainService.readRequest();
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
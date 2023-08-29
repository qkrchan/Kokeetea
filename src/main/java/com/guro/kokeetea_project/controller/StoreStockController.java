package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.RequestInfoDTO;
import com.guro.kokeetea_project.dto.StoreStockInfoDTO;
import com.guro.kokeetea_project.service.StoreStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class StoreStockController {

    private final StoreStockService storeStockService;

//    @GetMapping(value = {"/storeStock/mylist", "/storeStock/mylist/{page}"})
//    public String listStoreStock(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, Principal principal) {
//        try {
//            Pageable pageable = PageRequest.of(page.orElse(1) - 1, 5);
//            Page<StoreStockInfoDTO> storeStockList = storeStockService.list(pageable);
//
//            model.addAttribute("storeStocks", storeStockList);
//            model.addAttribute("maxPage", 5);
//            model.addAttribute("page", pageable.getPageNumber() + 1);
//        } catch (Exception e) {
//            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
//            model.addAttribute("username", principal.getName());
//            return "redirect:/";
//        }
//        model.addAttribute("username", principal.getName());
//        return "mylist";
//    }
//}

    @GetMapping(value = {"/storeStock/mylist", "/storeStock/mylist/{page}"}) //
    public String mylistStoreStock(@PathVariable("page") Optional<Integer> page, Principal principal, Model model, RedirectAttributes flash) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(1) - 1, 10);
            Page<StoreStockInfoDTO> storeStockList = storeStockService.mylist(pageable, principal.getName());

            model.addAttribute("storeStocks", storeStockList);
            model.addAttribute("page", pageable.getPageNumber() + 1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "storeStock/mylist";
    }
}

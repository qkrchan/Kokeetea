package com.guro.kokeetea_project.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import com.guro.kokeetea_project.dto.WarehouseStockInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.guro.kokeetea_project.dto.RequestFormDTO;
import com.guro.kokeetea_project.dto.RequestInfoDTO;
import com.guro.kokeetea_project.service.RequestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping(value = {"/request/list","/request/list/{page}"})
    public String listRequest(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, Principal principal){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 10);
            Page<RequestInfoDTO> requestList = requestService.list(pageable);

            model.addAttribute("requests", requestList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "request/list";
    }

    @GetMapping(value = {"/request/mylist","/request/mylist/{page}"})
    public String mylistRequest(@PathVariable("page") Optional<Integer> page, Principal principal, Model model, RedirectAttributes flash){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 10);
            Page<RequestInfoDTO> requestList = requestService.mylist(pageable, principal.getName());

            model.addAttribute("requests", requestList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "request/mylist";
    }

    @GetMapping(value = "/request/create")
    public String createRequest(Model model, RedirectAttributes flash, Principal principal){
        try {
            model.addAttribute("requestFormDTO", new RequestFormDTO());
            model.addAttribute("ingredients", requestService.ingredients());
            model.addAttribute("stores", requestService.stores());
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/request/mylist";
        }
        model.addAttribute("username", principal.getName());
        return "request/create";
    }

    @PostMapping(value = "/request/create")
    public String createRequestPost(@Valid RequestFormDTO requestFormDTO, BindingResult bindingResult, Model model, RedirectAttributes flash, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("ingredients", requestService.ingredients());
                model.addAttribute("stores", requestService.stores());
                model.addAttribute("username", principal.getName());
                return "request/create";
            }
            requestService.create(requestFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "발주 등록 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/request/mylist";
    }

    @PostMapping(value = "/request/confirm/{id}")
    public @ResponseBody ResponseEntity<String> confirmRequest(@PathVariable("id") Long id) {
        try {
            requestService.confirm(id);
        } catch (Exception e) {
            return new ResponseEntity<>("발주 승인 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/request/mycomplete/{id}")
    public @ResponseBody ResponseEntity<String> mycompleteRequest(@PathVariable("id") Long id) {
        try {
            requestService.complete(id);
        } catch (Exception e) {
            return new ResponseEntity<>("배송 확인 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/request/reject/{id}")
    public @ResponseBody ResponseEntity<String> rejectRequest(@PathVariable("id") Long id) {
        try {
            requestService.reject(id);
        } catch (Exception e) {
            return new ResponseEntity<>("발주 반려 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/request/cancel/{id}")
    public @ResponseBody ResponseEntity<String> cancelRequest(@PathVariable("id") Long id) {
        try {
            requestService.cancel(id);
        } catch (Exception e) {
            return new ResponseEntity<>("발주 취소 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/request/mycancel/{id}")
    public @ResponseBody ResponseEntity<String> mycancelRequest(@PathVariable("id") Long id) {
        try {
            requestService.cancel(id);
        } catch (Exception e) {
            return new ResponseEntity<>("발주 취소 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/request/{id}/refresh")
    public String refreshRequest(Optional<Integer> page,@PathVariable("id") String id, Model model, Principal principal) throws Exception {
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 10);
            Page<RequestInfoDTO> requestList = requestService.refreshList(pageable, id);

            model.addAttribute("requests", requestList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        }
        model.addAttribute("username", principal.getName());
        return "request/list :: #request-data";
    }
}

package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.*;
import com.guro.kokeetea_project.service.IngredientService;
import com.guro.kokeetea_project.service.SupplierService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping(value = {"/supplier/list","/supplier/list/{page}"})
    public String listSupplier(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, Principal principal){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 5);
            Page<SupplierInfoDTO> supplierList = supplierService.list(pageable);

            model.addAttribute("suppliers", supplierList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "supplier/list";
    }



    @GetMapping(value = "/supplier/create")
    public String createSupplier(Model model, Principal principal){
        model.addAttribute("supplierFormDTO", new SupplierFormDTO());
        model.addAttribute("username", principal.getName());
        return "supplier/create";
    }

    @PostMapping(value = "/supplier/create")
    public String createSupplierPost(@Valid SupplierFormDTO supplierFormDTO, BindingResult bindingResult, RedirectAttributes flash, Model model, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("username", principal.getName());
                return "supplier/create";
            }
            supplierService.create(supplierFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "공급사 등록 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/supplier/list";
    }

    @GetMapping(value = "/supplier/update/{id}")
    public String updateSupplier(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal){
        try {
            SupplierFormDTO supplierFormDTO = supplierService.original(id);
            model.addAttribute("supplierFormDTO", supplierFormDTO);
            model.addAttribute("username", principal.getName());
            return "supplier/create";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/supplier/list";
        }
    }

    @PostMapping(value = "/supplier/delete/{id}")
    public @ResponseBody ResponseEntity<String> deleteSupplierPost(@PathVariable("id") Long id) {
        try {
            supplierService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>("공급사 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/supplier/delete/{id}/full")
    public @ResponseBody ResponseEntity<String> deleteFullSupplierPost(@PathVariable("id") Long id) {
        try {
            supplierService.deleteFull(id);
        } catch (Exception e) {
            return new ResponseEntity<>("공급사 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

}

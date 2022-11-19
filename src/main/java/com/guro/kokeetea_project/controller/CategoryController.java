package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.CategoryFormDTO;
import com.guro.kokeetea_project.dto.CategoryInfoDTO;
import com.guro.kokeetea_project.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(value = {"/category/list","/category/list/{page}"})
    public String listCategory(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, Principal principal){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 5);
            Page<CategoryInfoDTO> categoryList = categoryService.list(pageable);
            
            model.addAttribute("categories", categoryList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "category/list";
    }

    @GetMapping(value = "/category/create")
    public String createCategory(Model model, Principal principal){
        model.addAttribute("categoryFormDTO", new CategoryFormDTO());
        model.addAttribute("username", principal.getName());
        return "category/create";
    }

    @PostMapping(value = "/category/create")
    public String createCategoryPost(@Valid CategoryFormDTO categoryFormDTO, BindingResult bindingResult, RedirectAttributes flash, Model model, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("username", principal.getName());
                return "category/create";
            }
            categoryService.create(categoryFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "재료 등록 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/category/list";
    }

    @GetMapping(value = "/category/update/{id}")
    public String updateCategory(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal){
        try {
            CategoryFormDTO categoryFormDTO = categoryService.original(id);
            model.addAttribute("categoryFormDTO", categoryFormDTO);
            model.addAttribute("username", principal.getName());
            return "category/create";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/category/list";
        }
    }

    @PostMapping(value = "/category/update")
    public String updateCategoryPost(@Valid CategoryFormDTO categoryFormDTO, BindingResult bindingResult, RedirectAttributes flash, Model model, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("username", principal.getName());
                return "category/create";
            }
            categoryService.update(categoryFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "재료 수정 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/category/list";
    }

    @PostMapping(value = "/category/delete/{id}")
    public @ResponseBody ResponseEntity<String> deleteCategoryPost(@PathVariable("id") Long id) {
        try {
            categoryService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>("재료 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/category/delete/{id}/full")
    public @ResponseBody ResponseEntity<String> deleteFullCategoryPost(@PathVariable("id") Long id) {
        try {
            categoryService.deleteFull(id);
        } catch (Exception e) {
            return new ResponseEntity<>("재료 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }
}

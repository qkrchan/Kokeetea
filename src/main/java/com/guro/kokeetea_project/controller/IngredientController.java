package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.IngredientFormDTO;
import com.guro.kokeetea_project.dto.IngredientInfoDTO;
import com.guro.kokeetea_project.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping(value = {"/ingredient/list","/ingredient/list/{page}"})
    public String listIngredient(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, Principal principal){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 5);
            Page<IngredientInfoDTO> ingredientList = ingredientService.list(pageable);
            
            model.addAttribute("ingredients", ingredientList);
            model.addAttribute("page", pageable.getPageNumber()+1);
            model.addAttribute("maxPage", 5);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "ingredient/list";
    }

    @GetMapping(value = "/ingredient/create")
    public String createIngredient(Model model, RedirectAttributes flash, Principal principal){
        try {
            model.addAttribute("ingredientFormDTO", new IngredientFormDTO());
            model.addAttribute("categories", ingredientService.categories());
            model.addAttribute("suppliers", ingredientService.suppliers());
            model.addAttribute("username", principal.getName());
            return "ingredient/create";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/ingredient/list";
        }
    }

    @PostMapping(value = "/ingredient/create")
    public String createIngredientPost(@Valid IngredientFormDTO ingredientFormDTO, BindingResult bindingResult, Model model, RedirectAttributes flash, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("categories", ingredientService.categories());
                model.addAttribute("suppliers", ingredientService.suppliers());
                model.addAttribute("username", principal.getName());
                return "ingredient/create";
            }
            ingredientService.create(ingredientFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "재료 등록 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/ingredient/list";
    }

    @GetMapping(value = "/ingredient/update/{id}")
    public String updateIngredient(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal){
        try {
            IngredientFormDTO ingredientFormDTO = ingredientService.original(id);
            model.addAttribute("ingredientFormDTO", ingredientFormDTO);
            model.addAttribute("categories", ingredientService.categories());
            model.addAttribute("username", principal.getName());
            return "ingredient/create";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/ingredient/list";
        }
    }

    @PostMapping(value = "/ingredient/update")
    public String updateIngredientPost(@Valid IngredientFormDTO ingredientFormDTO, BindingResult bindingResult, Model model, RedirectAttributes flash, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("categories", ingredientService.categories());
                model.addAttribute("username", principal.getName());
                return "ingredient/create";
            }
            ingredientService.update(ingredientFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "재료 수정 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/ingredient/list";
    }

    @PostMapping(value = "/ingredient/delete/{id}")
    public @ResponseBody ResponseEntity<String> deleteIngredientPost(@PathVariable("id") Long id) {
        try {
            ingredientService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>("재료 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/ingredient/delete/{id}/full")
    public @ResponseBody ResponseEntity<String> deleteFullIngredientPost(@PathVariable("id") Long id) {
        try {
            ingredientService.deleteFull(id);
        } catch (Exception e) {
            return new ResponseEntity<>("재료 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }
}

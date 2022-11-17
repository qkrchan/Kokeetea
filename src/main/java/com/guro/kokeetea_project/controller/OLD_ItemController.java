package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.OLD_ItemFormDto;
import com.guro.kokeetea_project.entity.OLD_Item;
import com.guro.kokeetea_project.service.OLD_ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OLD_ItemController {

    private final OLD_ItemService OLDItemService;


    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new OLD_ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid OLD_ItemFormDto OLDItemFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        try {
            OLDItemService.saveItem(OLDItemFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            OLD_ItemFormDto OLDItemFormDto = OLDItemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", OLDItemFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto",new OLD_ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid OLD_ItemFormDto OLDItemFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        try {
            OLDItemService.updateItem(OLDItemFormDto);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(@PathVariable("page")Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3);

        Page<OLD_Item> items = OLDItemService.getAdminItemPage(pageable);
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        OLD_ItemFormDto OLDItemFormDto = OLDItemService.getItemDtl(itemId);
        model.addAttribute("item", OLDItemFormDto);
        return "item/itemDtl";
    }

}
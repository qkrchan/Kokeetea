package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.CurrentStockFormDTO;
import com.guro.kokeetea_project.dto.*;
import com.guro.kokeetea_project.service.WarehouseService;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping(value = {"/warehouse/list","/warehouse/list/{page}"})
    public String listWarehouse(@PathVariable("page") Optional<Integer> page, Model model, RedirectAttributes flash, SearchDTO searchDTO, Principal principal){
        try {
            Pageable pageable = PageRequest.of(page.orElse(1)-1, 5);
            Page<WarehouseInfoDTO> warehouseList = warehouseService.list(pageable);

            model.addAttribute("warehouseFilter", warehouseService.warehouses());
            model.addAttribute("ingredientFilter", warehouseService.ingredients());
            model.addAttribute("searchDTO",searchDTO);
            model.addAttribute("warehouses", warehouseList);
            model.addAttribute("maxPage", 5);
            model.addAttribute("page", pageable.getPageNumber()+1);
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "목록 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/";
        }
        model.addAttribute("username", principal.getName());
        return "warehouse/list";
    }

    @GetMapping(value = "/warehouse/create")
    public String createWarehouse(Model model, Principal principal){
        model.addAttribute("warehouseFormDTO", new WarehouseFormDTO());
        model.addAttribute("username", principal.getName());
        return "warehouse/create";
    }

    @PostMapping(value = "/warehouse/create")
    public String createWarehousePost(@Valid WarehouseFormDTO warehouseFormDTO, BindingResult bindingResult, Model model, Principal principal){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("username", principal.getName());
                return "warehouse/create";
            }
            warehouseService.create(warehouseFormDTO);
        } catch (Exception e){
            model.addAttribute("errorMessage", "창고 등록 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/warehouse/list";
    }

    @GetMapping(value = "/warehouse/update/{id}")
    public String updateWarehouse(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal){
        try {
            WarehouseFormDTO warehouseFormDTO = warehouseService.original(id);
            model.addAttribute("warehouseFormDTO", warehouseFormDTO);
            model.addAttribute("username", principal.getName());
            return "warehouse/create";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/warehouse/list";
        }
    }

    @PostMapping(value = "/warehouse/update")
    public String updateWarehousePost(@Valid WarehouseFormDTO warehouseFormDTO, BindingResult bindingResult, RedirectAttributes flash, Principal principal, Model model){
        try {
            if (bindingResult.hasErrors()){
                model.addAttribute("username", principal.getName());
                return "warehouse/create";
            }
            warehouseService.update(warehouseFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "창고 수정 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:/warehouse/list";
    }

    @PostMapping(value = "/warehouse/delete/{id}")
    public @ResponseBody ResponseEntity<String> deleteWarehousePost(@PathVariable("id") Long id) {
        try {
            warehouseService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>("창고 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @PostMapping(value = "/warehouse/delete/{id}/full")
    public @ResponseBody ResponseEntity<String> deleteFullWarehousePost(@PathVariable("id") Long id) {
        try {
            warehouseService.deleteFull(id);
        } catch (Exception e) {
            return new ResponseEntity<>("창고 삭제 중 에러가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(String.valueOf(id), HttpStatus.OK);
    }

    @GetMapping(value = "/warehouse/{id}")
    public String viewWarehouse(@PathVariable("id") Long id, Model model, RedirectAttributes flash, Principal principal) throws Exception {
        try {
            WarehouseStockInfoDTO warehouseStockInfoDTO = warehouseService.status(id);
            Map<String, Long> itemsSum = warehouseService.chartWarehouse(warehouseStockInfoDTO);
            model.addAttribute("warehouse", warehouseStockInfoDTO);
            model.addAttribute("data", itemsSum);
            model.addAttribute("username", principal.getName());
            return "warehouse/view";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "창고 조회 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "warehouse/list";
        }
    }

    @GetMapping(value = "/warehouse/ingredient/{id}")
    public String viewWarehouseByIngredient(@PathVariable("id") Long id, Model model, Principal principal) throws Exception {
        try {
            IngredientStockInfoDTO ingredientStockInfoDTO = warehouseService.status2(id);
            model.addAttribute("ingredient", ingredientStockInfoDTO);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("username", principal.getName());
        return "warehouse/ingredient/view";
    }

    @PostMapping(value = "/warehouse/{id}/refresh")
    public String refreshWarehouse(@PathVariable("id") Long id, Model model, Principal principal) throws Exception {
        try {
            WarehouseStockInfoDTO warehouseList = warehouseService.status(id);
            model.addAttribute("warehouse", warehouseList);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("username", principal.getName());
        return "warehouse/view :: #warehouse-data";
    }

    @GetMapping(value = {"/warehouse/currentstock/{id}", "/warehouse/currentstock/{id}/{type}"})
    public String updateCurrentStock(@PathVariable("id") Long id, @PathVariable("type") Optional<String> type, Model model, RedirectAttributes flash, Principal principal){
        try {
            Boolean isIngredient = type.map(s -> s.equals("ingredient")).orElse(false);
            CurrentStockFormDTO currentStockFormDTO = warehouseService.originalCurrentStock(id, isIngredient);
            model.addAttribute("currentStockFormDTO", currentStockFormDTO);
            model.addAttribute("username", principal.getName());
            return "warehouse/currentstock";
        } catch (Exception e) {
            flash.addFlashAttribute("errorMessage", "양식 표시 중 에러가 발생하였습니다.");
            model.addAttribute("username", principal.getName());
            return "redirect:/warehouse/list";
        }
    }

    @PostMapping(value = "/warehouse/currentstock")
    public String updateCurrentStockPost(@Valid CurrentStockFormDTO currentStockFormDTO, BindingResult bindingResult, RedirectAttributes flash, Model model, Principal principal){
        if (bindingResult.hasErrors()){
            model.addAttribute("username", principal.getName());
            return "warehouse/currentstock";
        }
        try {
            warehouseService.updateCurrentStock(currentStockFormDTO);
        } catch (Exception e){
            flash.addFlashAttribute("errorMessage", "재고 갱신 중 에러가 발생하였습니다.");
        }
        model.addAttribute("username", principal.getName());
        return "redirect:"+currentStockFormDTO.getSourceUrl();
    }

    @PostMapping(value = "/warehouse/ingredient/{id}/refresh")
    public String refreshIngredient(@PathVariable("id") Long id, Model model, Principal principal) throws Exception {
        try {
            IngredientStockInfoDTO ingredientStockInfoDTO = warehouseService.status2(id);
            model.addAttribute("ingredients", ingredientStockInfoDTO);
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("username", principal.getName());
        return "warehouse/view2 :: #warehouse-data";
    }




    /*@GetMapping(value = "/chart")
    public String chart(Model model){
        List<WarehouseStockInfoDTO> dtoList = warehouseService.dtoList();
        Map<String, Long> itemsSum = new TreeMap<>();

        for(CurrentStockInfoDTO dto : dtoList.get(0).getCurrentStockInfoDTOList()){
            itemsSum.put(dto.getName(), dto.getAmount());
        }


        model.addAttribute("data", itemsSum);

        return "/warehouse/chart";
    }*/
    //test data
    /*Map<String, Integer> getData(){
        Map<String, Integer> itemsSum = new TreeMap<>();

        for(int i=1; i<=30; i++){
            itemsSum.put(String.valueOf(i), (int)((Math.random()+1)*10)*15000);
        }

        return itemsSum;
    }*/

}

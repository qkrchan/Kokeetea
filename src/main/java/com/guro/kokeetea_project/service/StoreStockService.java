package com.guro.kokeetea_project.service;

import com.guro.kokeetea_project.dto.StoreStockInfoDTO;
import com.guro.kokeetea_project.entity.StoreStock;
import com.guro.kokeetea_project.repository.StoreRepository;
import com.guro.kokeetea_project.repository.StoreStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreStockService {

    private final StoreRepository storeRepository;
    private final StoreStockRepository storeStockRepository;

    @Transactional(readOnly = true)
    public Page<StoreStockInfoDTO> mylist(Pageable pageable, String name) throws Exception {
        List<StoreStock> storeStocks = storeStockRepository.listStoreStock(pageable);
        Long totalCount = storeStockRepository.countStoreStock();
        List<StoreStockInfoDTO> list = new ArrayList<>();

        for (StoreStock storeStock : storeStocks){
            StoreStockInfoDTO dto = new StoreStockInfoDTO();
            dto.setId(storeStock.getId());
            dto.setStore(storeStock.getStore());
            dto.setMilk(storeStock.getMilk());
            dto.setTealeaf(storeStock.getTealeaf());
            dto.setSugar(storeStock.getSugar());
            dto.setHoney(storeStock.getHoney());
            dto.setOreo(storeStock.getOreo());
            dto.setBean(storeStock.getBean());
            dto.setCoconut(storeStock.getCoconut());
            dto.setCondensed_milk(storeStock.getCondensed_milk());
            dto.setMatcha(storeStock.getMatcha());
            dto.setStrawberry(storeStock.getStrawberry());
            dto.setBrown_sugar(storeStock.getBrown_sugar());
            dto.setDragon_fruit(storeStock.getDragon_fruit());
            dto.setRose_flavor(storeStock.getRose_flavor());
            dto.setOoLong(storeStock.getOoLong());
            dto.setPeach(storeStock.getPeach());
            dto.setStrawberry_flavor(storeStock.getStrawberry_flavor());
            dto.setLemon_flavor(storeStock.getLemon_flavor());
            dto.setLychee_flavor(storeStock.getLychee_flavor());
            dto.setMango(storeStock.getMango());
            dto.setPassionfruit_flavor(storeStock.getPassionfruit_flavor());
            dto.setTaro(storeStock.getTaro());
            dto.setOrange_tea(storeStock.getOrange_tea());
            dto.setGrapefruit_flavor(storeStock.getGrapefruit_flavor());
            dto.setBlack_tea(storeStock.getBlack_tea());
            dto.setWhite_grape(storeStock.getWhite_grape());
            dto.setKiwi_flavor(storeStock.getKiwi_flavor());
            dto.setSweet_cloud(storeStock.getSweet_cloud());
            dto.setGreen_tea(storeStock.getGreen_tea());
            dto.setDalgona_foam(storeStock.getDalgona_foam());
            dto.setPassion_fruit(storeStock.getPassion_fruit());
            dto.setPina_colada(storeStock.getPina_colada());
            dto.setSyrup(storeStock.getSyrup());
            dto.setCream(storeStock.getCream());
            dto.setPassionfruit_jam(storeStock.getPassionfruit_jam());
            dto.setMango_passionfruit(storeStock.getMango_passionfruit());

            list.add(dto);
        }

        return new PageImpl<>(list, pageable, totalCount);
    }

//    @Transactional(readOnly = true)
//    public StoreStockFormDTO original(Long id) throws Exception {
//        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
//        if (!ingredient.getIsValid()) {
//            throw new EntityNotFoundException();
//        }
//        IngredientFormDTO dto = new IngredientFormDTO();
//        dto.setId(ingredient.getId());
//        dto.setName(ingredient.getName());
//        dto.setPrice(ingredient.getPrice());
//        if (ingredient.getCategory()!=null && ingredient.getCategory().getIsValid()) {
//            dto.setCategoryId(ingredient.getCategory().getId());
//        }
//        if (ingredient.getSupplier()!=null &&  ingredient.getSupplier().getIsValid()) {
//            dto.setSupplierId(ingredient.getSupplier().getId());
//        }

//    @Transactional(readOnly = true)
//    public StoreStockInfoDTO original(Long id) throws Exception {
//        StoreStock storeStock = storeStockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
//        if (!storeStock.getIsValid()) {
//            throw new EntityNotFoundException();
//        }
//        StoreStockInfoDTO dto = new StoreStockInfoDTO();
//        dto.setId(supplier.getId());
//        supplier.setName(supplier.getName());
//        supplier.setPhone(supplier.getPhone());
//        supplier.setLocation(supplier.getLocation());
//        supplier.setEmail(supplier.getEmail());
//        supplier.setMaterial(supplier.getMaterial());
//        supplier.setOrigin(supplier.getOrigin());
//
//        return dto;
//    }
//
//
//    public Long create(SupplierFormDTO supplierFormDTO) throws Exception {
//        Supplier supplier = new Supplier();
//        supplier.setName(supplierFormDTO.getName());
//        supplier.setPhone(supplierFormDTO.getPhone());
//        supplier.setLocation(supplierFormDTO.getLocation());
//        supplier.setEmail(supplierFormDTO.getEmail());
//        supplier.setMaterial(supplierFormDTO.getMaterial());
//        supplier.setOrigin(supplierFormDTO.getOrigin());
//
//        supplier.setIsValid(true);
//        supplierRepository.save(supplier);
//
//        return supplier.getId();
//    }
//
//
//    public Long update(SupplierFormDTO supplierFormDTO) throws Exception {
//        Supplier supplier = supplierRepository.findById(supplierFormDTO.getId()).orElseThrow(EntityNotFoundException::new);
//        if (!supplier.getIsValid()) {
//            throw new EntityNotFoundException();
//        }
//        supplier.setName(supplierFormDTO.getName());
//        supplierRepository.save(supplier);
//
//        return supplier.getId();
//    }
//
//    public void deleteFull(Long id) throws Exception {
//        Supplier supplier = supplierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
//        ingredientRepository.deleteBySupplier(supplier);
//        supplierRepository.deleteById(id);
//    }
//
//
//
//    public void delete(Long id) throws Exception {
//        Supplier supplier = supplierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
//        supplier.setIsValid(false);
//    }

}

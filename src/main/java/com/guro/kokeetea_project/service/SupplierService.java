package com.guro.kokeetea_project.service;


import com.guro.kokeetea_project.constant.RequestStatus;
import com.guro.kokeetea_project.dto.*;

import com.guro.kokeetea_project.entity.*;
import com.guro.kokeetea_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SupplierService {
    private final IngredientRepository ingredientRepository;
    private final SupplierRepository supplierRepository;

    @Transactional(readOnly = true)
    public Page<SupplierInfoDTO> list(Pageable pageable) throws Exception {
        List<Supplier> suppliers = supplierRepository.listSupplier(pageable);
        Long totalCount = supplierRepository.countSupplier();
        List<SupplierInfoDTO> list = new ArrayList<>();

        for (Supplier supplier : suppliers){
            SupplierInfoDTO dto = new SupplierInfoDTO();
            dto.setId(supplier.getId());
            dto.setName(supplier.getName());
            dto.setPhone(supplier.getPhone());
            dto.setLocation(supplier.getLocation());
            dto.setEmail(supplier.getEmail());
            dto.setMaterial(supplier.getMaterial());
            dto.setOrigin(supplier.getOrigin());

            list.add(dto);
        }

        return new PageImpl<>(list, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public SupplierFormDTO original(Long id) throws Exception {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!supplier.getIsValid()) {
            throw new EntityNotFoundException();
        }
        SupplierFormDTO dto = new SupplierFormDTO();
        dto.setId(supplier.getId());
        supplier.setName(supplier.getName());
        supplier.setPhone(supplier.getPhone());
        supplier.setLocation(supplier.getLocation());
        supplier.setEmail(supplier.getEmail());
        supplier.setMaterial(supplier.getMaterial());
        supplier.setOrigin(supplier.getOrigin());

        return dto;
    }


    public Long create(SupplierFormDTO supplierFormDTO) throws Exception {
        Supplier supplier = new Supplier();
        supplier.setName(supplierFormDTO.getName());
        supplier.setPhone(supplierFormDTO.getPhone());
        supplier.setLocation(supplierFormDTO.getLocation());
        supplier.setEmail(supplierFormDTO.getEmail());
        supplier.setMaterial(supplierFormDTO.getMaterial());
        supplier.setOrigin(supplierFormDTO.getOrigin());

        supplier.setIsValid(true);
        supplierRepository.save(supplier);

        return supplier.getId();
    }


    public Long update(SupplierFormDTO supplierFormDTO) throws Exception {
        Supplier supplier = supplierRepository.findById(supplierFormDTO.getId()).orElseThrow(EntityNotFoundException::new);
        if (!supplier.getIsValid()) {
            throw new EntityNotFoundException();
        }
        supplier.setName(supplierFormDTO.getName());
        supplierRepository.save(supplier);

        return supplier.getId();
    }

    public void deleteFull(Long id) throws Exception {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ingredientRepository.deleteBySupplier(supplier);
        supplierRepository.deleteById(id);
    }



    public void delete(Long id) throws Exception {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        supplier.setIsValid(false);
    }
}

package com.guro.kokeetea_project.service;

import com.guro.kokeetea_project.dto.CategoryFormDTO;
import com.guro.kokeetea_project.dto.CategoryInfoDTO;
import com.guro.kokeetea_project.entity.Category;
import com.guro.kokeetea_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryInfoDTO> list(Pageable pageable) throws Exception {
        List<Category> categories = categoryRepository.listCategory(pageable);
        Long totalCount = categoryRepository.countCategory();
        List<CategoryInfoDTO> list = new ArrayList<>();

        for (Category category : categories){
            CategoryInfoDTO dto = new CategoryInfoDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            list.add(dto);
        }

        return new PageImpl<>(list, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public CategoryFormDTO original(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!category.getIsValid()) {
            throw new EntityNotFoundException();
        }
        CategoryFormDTO dto = new CategoryFormDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public Long create(CategoryFormDTO categoryFormDTO) throws Exception {
        Category category = new Category();
        category.setName(categoryFormDTO.getName());
        category.setIsValid(true);
        categoryRepository.save(category);

        return category.getId();
    }

    public Long update(CategoryFormDTO categoryFormDTO) throws Exception {
        Category category = categoryRepository.findById(categoryFormDTO.getId()).orElseThrow(EntityNotFoundException::new);
        if (!category.getIsValid()) {
            throw new EntityNotFoundException();
        }
        category.setName(categoryFormDTO.getName());
        categoryRepository.save(category);

        return category.getId();
    }

    public void deleteFull(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ingredientRepository.deleteByCategory(category);
        categoryRepository.deleteById(id);
    }

    public void delete(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        category.setIsValid(false);
    }
}

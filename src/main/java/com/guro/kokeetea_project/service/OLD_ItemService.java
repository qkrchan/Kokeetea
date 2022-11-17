package com.guro.kokeetea_project.service;

import com.guro.kokeetea_project.dto.OLD_ItemFormDto;
import com.guro.kokeetea_project.entity.OLD_Item;
import com.guro.kokeetea_project.repository.OLD_ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class OLD_ItemService {

    private final OLD_ItemRepository OLDItemRepository;

    public Long saveItem(OLD_ItemFormDto OLDItemFormDto) throws Exception{

        OLD_Item OLDItem = new OLD_Item();
        OLDItem.setId(OLDItemFormDto.getId());
        OLDItem.setItemNm(OLDItemFormDto.getItemNm());
        OLDItem.setItemDetail(OLDItemFormDto.getItemDetail());
        OLDItem.setOLDItemSellStatus(OLDItemFormDto.getOLDItemSellStatus());
        OLDItem.setPrice(OLDItemFormDto.getPrice());
        OLDItem.setStockNumber(OLDItemFormDto.getStockNumber());
        OLDItemRepository.save(OLDItem);

        return OLDItem.getId();
    }

    @Transactional(readOnly = true)
    public OLD_ItemFormDto getItemDtl(Long itemId){
        OLD_Item OLDItem = OLDItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        OLD_ItemFormDto OLDItemFormDto = new OLD_ItemFormDto();
        OLDItemFormDto.setId(OLDItem.getId());
        OLDItemFormDto.setItemNm(OLDItem.getItemNm());
        OLDItemFormDto.setItemDetail(OLDItem.getItemDetail());
        OLDItemFormDto.setOLDItemSellStatus(OLDItem.getOLDItemSellStatus());
        OLDItemFormDto.setPrice(OLDItem.getPrice());
        OLDItemFormDto.setStockNumber(OLDItem.getStockNumber());
        return OLDItemFormDto;
    }

    public Long updateItem(OLD_ItemFormDto OLDItemFormDto) throws Exception{
        OLD_Item OLDItem = OLDItemRepository.findById(OLDItemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        OLDItem.updateItem(OLDItemFormDto);

        return OLDItem.getId();
    }

    @Transactional(readOnly = true)
    public Page<OLD_Item> getAdminItemPage(Pageable pageable){
        List<OLD_Item> result = this.OLDItemRepository.findAll();
        return new PageImpl<>(result, pageable, result.size());
    }
}

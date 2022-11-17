package com.guro.kokeetea_project.service;


import com.guro.kokeetea_project.repository.IngredientRepository;
import com.guro.kokeetea_project.repository.RequestRepository;
import com.guro.kokeetea_project.repository.StoreRepository;
import com.guro.kokeetea_project.repository.mainPage.MainAvgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MainService {

    private final RequestRepository requestRepository;
    private final IngredientRepository ingredientRepository;
    private final StoreRepository storeRepository;
    private final MainAvgRepository mainAvgRepository;

    public List<Integer> readRequest(){
        List<Integer>mainAvgList = mainAvgRepository.listRequest();
        return mainAvgList;
    }

    public List<Integer> ingredindCount(){
        List<Integer>mainAvgList = mainAvgRepository.ingredindCount();
        return mainAvgList;
    }

    public Object monthCount(){
        Object monthCount = mainAvgRepository.monthCount();
        return monthCount;
    }

    public List<Integer> storeCount(){
        List<Integer> storeCount = mainAvgRepository.storeCount();
        return storeCount;
    }
}

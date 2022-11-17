package com.guro.kokeetea_project.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityNotFoundException;

import com.guro.kokeetea_project.dto.*;
import com.guro.kokeetea_project.entity.CurrentStock;
import com.guro.kokeetea_project.repository.CurrentStockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.guro.kokeetea_project.constant.RequestStatus;
import com.guro.kokeetea_project.entity.Ingredient;
import com.guro.kokeetea_project.entity.Request;
import com.guro.kokeetea_project.entity.Store;
import com.guro.kokeetea_project.entity.Warehouse;
import com.guro.kokeetea_project.repository.IngredientRepository;
import com.guro.kokeetea_project.repository.RequestRepository;
import com.guro.kokeetea_project.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final IngredientRepository ingredientRepository;
    private final StoreRepository storeRepository;
    private final CurrentStockRepository currentStockRepository;

    @Transactional(readOnly = true)
    public StatisticsDTO stat() throws Exception {
        StatisticsDTO stat = new StatisticsDTO();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastmonthStart = now.toLocalDate().minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime thismonthStart = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastyearStart = now.toLocalDate().minusMonths(12).withDayOfMonth(1).atStartOfDay();
        stat.setStatOfRequestByMonth(requestRepository.countByMonth(lastyearStart, thismonthStart));
        stat.setStatOfRequestByMonthIngredient(requestRepository.countByMonthIngredient(lastmonthStart, thismonthStart));
        stat.setStatOfRequestByMonthStore(requestRepository.countByMonthStore(lastmonthStart, thismonthStart));
        return stat;
    }

    @Transactional(readOnly = true)
    public Page<RequestInfoDTO> list(Pageable pageable) throws Exception {
        List<Request> requests = requestRepository.listRequest(pageable);
        Long totalCount = requestRepository.countRequest();
        List<RequestInfoDTO> list = new ArrayList<>();

        for (Request request : requests){
            RequestInfoDTO dto = new RequestInfoDTO();
            try {
                dto.setId(request.getId());
                dto.setIngredientName(request.getIngredient()!=null?request.getIngredient().getName():null);
                dto.setAmount(request.getAmount());
                dto.setStoreName(request.getStore()!=null?request.getStore().getName():null);
                dto.setDate(request.getDate()!=null?request.getDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분").withLocale(Locale.forLanguageTag("ko"))):null);
                dto.setWarehouseName(request.getWarehouse()!=null?request.getWarehouse().getName():"미배정");
                if (dto.getIngredientName()==null || dto.getAmount()==null || dto.getStoreName()==null || dto.getDate()==null || dto.getWarehouseName()==null) {
                    throw new Exception();
                }
                switch (request.getStatus()) {
                    case PENDING : dto.setStatus("대기"); dto.setCanConfirm(true); dto.setCanReject(true); break;
                    case INPROGRESS : dto.setStatus("배송중"); dto.setCanCancel(true); break;
                    case COMPLETE : dto.setStatus("완료"); break;
                    case REJECTED : dto.setStatus("반려"); break;
                    case CANCELLED : dto.setStatus("취소"); break;
                    case NEEDACTION : dto.setStatus("확인필요"); break;
                    case ERROR : throw new Exception();
                    default : throw new Exception();
                }
            } catch (Exception e) {
                dto.setStatus("에러");
            }
            if (!dto.getStatus().equals("에러")) {
                list.add(dto);
            }
        }

        return new PageImpl<>(list, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public Page<RequestInfoDTO> mylist(Pageable pageable, String email) throws Exception {
        List<Request> requests = requestRepository.mylistRequest(email, pageable);
        Long totalCount = requestRepository.mycountRequest(email);
        List<RequestInfoDTO> list = new ArrayList<>();

        for (Request request : requests){
            RequestInfoDTO dto = new RequestInfoDTO();
            try {
                dto.setId(request.getId());
                dto.setIngredientName(request.getIngredient()!=null?request.getIngredient().getName():null);
                dto.setAmount(request.getAmount());
                dto.setDate(request.getDate()!=null?request.getDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분").withLocale(Locale.forLanguageTag("ko"))):null);
                dto.setWarehouseName(request.getWarehouse()!=null?request.getWarehouse().getName():"미배정");
                if (dto.getIngredientName()==null || dto.getAmount()==null || dto.getDate()==null || dto.getWarehouseName()==null) {
                    throw new Exception();
                }
                switch (request.getStatus()) {
                    case PENDING : dto.setStatus("대기"); dto.setCanCancel(true); break;
                    case INPROGRESS : dto.setStatus("배송중"); dto.setCanComplete(true); dto.setCanCancel(true); break;
                    case COMPLETE : dto.setStatus("완료"); break;
                    case REJECTED : dto.setStatus("반려"); break;
                    case CANCELLED : dto.setStatus("취소"); break;
                    case NEEDACTION : dto.setStatus("확인필요"); break;
                    case ERROR : throw new Exception();
                    default : throw new Exception();
                }
            } catch (Exception e) {
                dto.setStatus("에러");
            }
            if (!dto.getStatus().equals("에러")) {
                list.add(dto);
            }
        }

        return new PageImpl<>(list, pageable, totalCount);
    }

    public Long create(RequestFormDTO requestFormDTO) throws Exception {
        Ingredient ingredient = ingredientRepository.findById(requestFormDTO.getIngredientId())
                .orElseThrow(EntityNotFoundException::new);
        if (!ingredient.getIsValid()) {
            throw new EntityNotFoundException();
        }
        Store store = storeRepository.findById(requestFormDTO.getStoreId())
                .orElseThrow(EntityNotFoundException::new);
        if (!store.getIsValid()) {
            throw new EntityNotFoundException();
        }

        Request request = new Request();
        request.setIngredient(ingredient);
        request.setAmount(requestFormDTO.getAmount());
        request.setStore(store);
        request.setStatus(RequestStatus.PENDING);

        Warehouse warehouse = store.getWarehouse();
        if (warehouse == null || !warehouse.getIsValid()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        for (CurrentStock currentStock : warehouse.getCurrentStock()){
            if(currentStock.getIngredient() == ingredient) {
                if (currentStock.getAmount() < requestFormDTO.getAmount()) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
                }
                request.setWarehouse(warehouse);
                break;
            }
        }

        requestRepository.save(request);

        return request.getId();
    }

    public void confirm(Long id) throws Exception {
        Request request = requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!request.getIngredient().getIsValid() || request.getAmount()<=0 || !request.getStore().getIsValid()
            || !request.getWarehouse().getIsValid() || request.getStatus()!=RequestStatus.PENDING) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        CurrentStock currentStock = currentStockRepository.findValidCurrentStock(request.getIngredient(), request.getWarehouse()).orElseThrow(EntityNotFoundException::new);
        if (currentStock.getAmount() < request.getAmount()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        currentStock.setAmount(currentStock.getAmount() - request.getAmount());
        request.setStatus(RequestStatus.INPROGRESS);
    }

    public void complete(Long id) throws Exception {
        Request request = requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!request.getIngredient().getIsValid() || request.getAmount()<=0 || !request.getStore().getIsValid()
                || !request.getWarehouse().getIsValid() || request.getStatus()!=RequestStatus.INPROGRESS) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        request.setStatus(RequestStatus.COMPLETE);
    }

    public void reject(Long id) throws Exception {
        Request request = requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!request.getIngredient().getIsValid() || request.getAmount()<=0 || !request.getStore().getIsValid()
                || !request.getWarehouse().getIsValid() || request.getStatus()!=RequestStatus.PENDING) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        request.setStatus(RequestStatus.REJECTED);
    }

    public void cancel(Long id) throws Exception {
        Request request = requestRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!request.getIngredient().getIsValid() || request.getAmount()<=0 || !request.getStore().getIsValid()
                || !request.getWarehouse().getIsValid() || (request.getStatus()!=RequestStatus.PENDING && request.getStatus()!=RequestStatus.INPROGRESS)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        if (request.getStatus()==RequestStatus.INPROGRESS) {
            CurrentStock currentStock = currentStockRepository.findValidCurrentStock(request.getIngredient(), request.getWarehouse()).orElseThrow(EntityNotFoundException::new);
            currentStock.setAmount(currentStock.getAmount() + request.getAmount());
        }
        request.setStatus(RequestStatus.CANCELLED);
    }

    @Transactional(readOnly = true)
    public List<IngredientInfoDTO> ingredients() throws Exception {
        List<Ingredient> ingredients = ingredientRepository.listIngredient();
        List<IngredientInfoDTO> list = new ArrayList<>();

        if (ingredients.isEmpty()) {
            throw new EntityNotFoundException();
        }

        for (Ingredient ingredient : ingredients) {
            IngredientInfoDTO dto = new IngredientInfoDTO();
            dto.setId(ingredient.getId());
            dto.setName(ingredient.getName());
            list.add(dto);
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<StoreInfoDTO> stores() throws Exception {
        List<Store> stores = storeRepository.listStore();
        List<StoreInfoDTO> list = new ArrayList<>();

        if (stores.isEmpty()) {
            throw new EntityNotFoundException();
        }

        for (Store store : stores) {
            StoreInfoDTO dto = new StoreInfoDTO();
            dto.setId(store.getId());
            dto.setName(store.getName());
            list.add(dto);
        }

        return list;
    }
}

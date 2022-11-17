package com.guro.kokeetea_project.service;

import com.guro.kokeetea_project.dto.OLD_OrderDto;
import com.guro.kokeetea_project.dto.OLD_OrderHistDto;
import com.guro.kokeetea_project.dto.OLD_OrderItemDto;
import com.guro.kokeetea_project.entity.OLD_Item;
import com.guro.kokeetea_project.entity.Member;
import com.guro.kokeetea_project.entity.OLD_Order;
import com.guro.kokeetea_project.entity.OLD_OrderItem;
import com.guro.kokeetea_project.repository.OLD_ItemRepository;
import com.guro.kokeetea_project.repository.MemberRepository;
import com.guro.kokeetea_project.repository.OLD_OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OLD_OrderService {

    private final OLD_ItemRepository OLDItemRepository;
    private final MemberRepository memberRepository;
    private final OLD_OrderRepository OLDOrderRepository;

    public Long order(OLD_OrderDto OLDOrderDto, String email){
        OLD_Item OLDItem = OLDItemRepository.findById(OLDOrderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OLD_OrderItem> OLDOrderItemList = new ArrayList<>();
        OLD_OrderItem OLDOrderItem = OLD_OrderItem.createOrderItem(OLDItem, OLDOrderDto.getCount());
        OLDOrderItemList.add(OLDOrderItem);

        OLD_Order OLDOrder = OLD_Order.createOrder(member, OLDOrderItemList);
        OLDOrderRepository.save(OLDOrder);

        return OLDOrder.getId();
    }

    @Transactional(readOnly = true)
    public Page<OLD_OrderHistDto> getOrderList(String email, Pageable pageable){

        List<OLD_Order> OLDOrders = OLDOrderRepository.findOrders(email, pageable);
        Long totalCount = OLDOrderRepository.countOrder(email);

        List<OLD_OrderHistDto> OLDOrderHistDtos = new ArrayList<>();

        for (OLD_Order OLDOrder : OLDOrders){
            OLD_OrderHistDto OLDOrderHistDto = new OLD_OrderHistDto(OLDOrder);
            List<OLD_OrderItem> OLDOrderItems = OLDOrder.getOLDOrderItems();
            for (OLD_OrderItem OLDOrderItem : OLDOrderItems) {
                OLD_OrderItemDto OLDOrderItemDto = new OLD_OrderItemDto(OLDOrderItem);
                OLDOrderHistDto.addOrderItemDto(OLDOrderItemDto);
            }

            OLDOrderHistDtos.add(OLDOrderHistDto);
        }

        return new PageImpl<OLD_OrderHistDto>(OLDOrderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        OLD_Order OLDOrder = OLDOrderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = OLDOrder.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        OLD_Order OLDOrder = OLDOrderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        OLDOrder.cancelOrder();
    }

    public Long orders(List<OLD_OrderDto> OLDOrderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        List<OLD_OrderItem> OLDOrderItemList = new ArrayList<>();

        for (OLD_OrderDto OLDOrderDto : OLDOrderDtoList){
            OLD_Item OLDItem = OLDItemRepository.findById(OLDOrderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OLD_OrderItem OLDOrderItem = OLD_OrderItem.createOrderItem(OLDItem, OLDOrderDto.getCount());
            OLDOrderItemList.add(OLDOrderItem);
        }

        OLD_Order OLDOrder = OLD_Order.createOrder(member, OLDOrderItemList);
        OLDOrderRepository.save(OLDOrder);

        return OLDOrder.getId();
    }
}

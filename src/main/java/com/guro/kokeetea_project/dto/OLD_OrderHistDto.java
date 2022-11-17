package com.guro.kokeetea_project.dto;

import com.guro.kokeetea_project.constant.OLD_OrderStatus;
import com.guro.kokeetea_project.entity.OLD_Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OLD_OrderHistDto {

    public OLD_OrderHistDto(OLD_Order OLDOrder){
        this.orderId = OLDOrder.getId();
        this.orderDate = OLDOrder.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.OLDOrderStatus = OLDOrder.getOLDOrderStatus();
    }

    private Long orderId;

    private String orderDate;

    private OLD_OrderStatus OLDOrderStatus;

    private List<OLD_OrderItemDto> OLDOrderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OLD_OrderItemDto OLDOrderItemDto){
        OLDOrderItemDtoList.add(OLDOrderItemDto);
    }
}

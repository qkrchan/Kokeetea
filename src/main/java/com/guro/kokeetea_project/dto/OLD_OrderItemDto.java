package com.guro.kokeetea_project.dto;

import com.guro.kokeetea_project.entity.OLD_OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OLD_OrderItemDto {

    public OLD_OrderItemDto(OLD_OrderItem OLDOrderItem){
        this.itemNm = OLDOrderItem.getOLDItem().getItemNm();
        this.count = OLDOrderItem.getCount();
        this.orderPrice = OLDOrderItem.getOrderPrice();
    }

    private String itemNm;

    private int count;

    private int orderPrice;
}

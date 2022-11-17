package com.guro.kokeetea_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OLD_OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private OLD_Item OLDItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OLD_Order OLDOrder;

    private int orderPrice;

    private int count;

    public static OLD_OrderItem createOrderItem(OLD_Item OLDItem, int count){
        OLD_OrderItem OLDOrderItem = new OLD_OrderItem();
        OLDOrderItem.setOLDItem(OLDItem);
        OLDOrderItem.setCount(count);
        OLDOrderItem.setOrderPrice(OLDItem.getPrice());

        OLDItem.removeStock(count);
        return OLDOrderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel(){
        this.getOLDItem().addStock(count);
    }
}

package com.guro.kokeetea_project.entity;

import com.guro.kokeetea_project.constant.OLD_OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class OLD_Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OLD_OrderStatus OLDOrderStatus;

    @OneToMany(mappedBy = "OLDOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OLD_OrderItem> OLDOrderItems = new ArrayList<>();

    public void addOrderItem(OLD_OrderItem OLDOrderItem){
        OLDOrderItems.add(OLDOrderItem);
        OLDOrderItem.setOLDOrder(this);
    }

    public static OLD_Order createOrder(Member member, List<OLD_OrderItem> OLDOrderItemList){
        OLD_Order OLDOrder = new OLD_Order();
        OLDOrder.setMember(member);
        for (OLD_OrderItem OLDOrderItem : OLDOrderItemList){
            OLDOrder.addOrderItem(OLDOrderItem);
        }
        OLDOrder.setOLDOrderStatus(OLD_OrderStatus.ORDER);
        OLDOrder.setOrderDate(LocalDateTime.now());
        return OLDOrder;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for (OLD_OrderItem OLDOrderItem : OLDOrderItems){
            totalPrice += OLDOrderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.OLDOrderStatus = OLD_OrderStatus.CANCLE;

        for (OLD_OrderItem OLDOrderItem : OLDOrderItems){
            OLDOrderItem.cancel();
        }
    }
}

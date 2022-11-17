package com.guro.kokeetea_project.entity;

import com.guro.kokeetea_project.constant.OLD_ItemSellStatus;
import com.guro.kokeetea_project.dto.OLD_ItemFormDto;
import com.guro.kokeetea_project.exception.OLD_OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class OLD_Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemNm;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private OLD_ItemSellStatus OLDItemSellStatus;


    public void updateItem(OLD_ItemFormDto OLDItemFormDto){
        this.itemNm = OLDItemFormDto.getItemNm();
        this.price = OLDItemFormDto.getPrice();
        this.stockNumber = OLDItemFormDto.getStockNumber();
        this.itemDetail = OLDItemFormDto.getItemDetail();
        this.OLDItemSellStatus = OLDItemFormDto.getOLDItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0){
            throw new OLD_OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");

        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}

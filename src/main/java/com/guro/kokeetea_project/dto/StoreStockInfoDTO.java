package com.guro.kokeetea_project.dto;

import com.guro.kokeetea_project.entity.Store;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class StoreStockInfoDTO {

    private Long id;
    private Store store;
    private Long milk;
    private Long tealeaf;
    private Long sugar;
    private Long honey;
    private Long oreo;
    private Long bean;
    private Long coconut;
    private Long condensed_milk;
    private Long matcha;
    private Long strawberry;
    private Long brown_sugar;
    private Long dragon_fruit;
    private Long rose_flavor;
    private Long ooLong;
    private Long peach;
    private Long strawberry_flavor;
    private Long lemon_flavor;
    private Long lychee_flavor;
    private Long mango;
    private Long passionfruit_flavor;
    private Long taro;
    private Long orange_tea;
    private Long grapefruit_flavor;
    private Long black_tea;
    private Long white_grape;
    private Long kiwi_flavor;
    private Long sweet_cloud;
    private Long green_tea;
    private Long dalgona_foam;
    private Long passion_fruit;
    private Long pina_colada;
    private Long syrup;
    private Long cream;
    private Long passionfruit_jam;
    private Long mango_passionfruit;
    private String storeName;
}

package com.guro.kokeetea_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "store_stock")
@Getter
@Setter
public class StoreStock {
    @Id
    @Column(name = "store_stock_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column
    private Long milk;

    @Column
    private Long tealeaf;

    @Column
    private Long sugar;

    @Column
    private Long honey;

    @Column
    private Long oreo;

    @Column
    private Long bean;

    @Column
    private Long coconut;

    @Column
    private Long condensed_milk;

    @Column
    private Long matcha;

    @Column
    private Long strawberry;

    @Column
    private Long brown_sugar;

    @Column
    private Long dragon_fruit;

    @Column
    private Long rose_flavor;

    @Column
    private Long ooLong;

    @Column
    private Long peach;

    @Column
    private Long strawberry_flavor;

    @Column
    private Long lemon_flavor;


    @Column
    private Long lychee_flavor;

    @Column
    private Long mango;

    @Column
    private Long passionfruit_flavor;

    @Column
    private Long taro;

    @Column
    private Long orange_tea;

    @Column
    private Long grapefruit_flavor;

    @Column
    private Long black_tea;

    @Column
    private Long white_grape;

    @Column
    private Long kiwi_flavor;

    @Column
    private Long sweet_cloud;

    @Column
    private Long green_tea;

    @Column
    private Long dalgona_foam;

    @Column
    private Long passion_fruit;

    @Column
    private Long pina_colada;

    @Column
    private Long syrup;

    @Column
    private Long cream;

    @Column
    private Long passionfruit_jam;

    @Column
    private Long mango_passionfruit;

    @Column
    private Boolean isValid;

}
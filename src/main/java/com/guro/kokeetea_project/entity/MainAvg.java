package com.guro.kokeetea_project.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class MainAvg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ingredient_avg;
    private Long ingredient_id;

//    private Long coff_avg; //커피평균 3363
//    private Long hongtea_avg; //홍차평균 3364
//    private Long greentea_avg; //녹차평균 3365
//    private Long milk_avg; //우유평균 3366
//    private Long choco_avg; //초콜릿평균 3367
//    private Long sinamon_avg; //시나몬평균 3368
//    private Long honey_avg; //꿀 평균 3369
//    private Long orange_avg; //오렌지 평균 3370
//    private Long graf_avg; //포도 평균 3371
//    private Long banana_avg; //바나나 평균 3372
//
//    private Long ingredient_count; //건수
//
//    private Long month_count; //월별 건수
//    private Long month_avg; //월별 평균 발주건수
//    private Long store_count;
//    private Long store_avg;
}

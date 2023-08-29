package com.guro.kokeetea_project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import com.guro.kokeetea_project.constant.RequestStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "request")
@Getter
@Setter
public class Request extends TimeEntity{

    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

//    @Column(columnDefinition = "TIMESTAMP")
//    private LocalDateTime date;
//    public Request() {
//        // 랜덤한 LocalDateTime 생성
//        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
//        LocalDateTime endDate = LocalDateTime.of(2023, 8, 31, 23, 59);
//
//        long startEpochSecond = startDate.toEpochSecond(ZoneOffset.UTC);
//        long endEpochSecond = endDate.toEpochSecond(ZoneOffset.UTC);
//
//        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);
//        date = LocalDateTime.ofEpochSecond(randomEpochSecond, 0, ZoneOffset.UTC);
//    }
}

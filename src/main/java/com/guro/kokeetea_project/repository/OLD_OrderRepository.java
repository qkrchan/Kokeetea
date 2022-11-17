package com.guro.kokeetea_project.repository;

import com.guro.kokeetea_project.entity.OLD_Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLD_OrderRepository extends JpaRepository<OLD_Order,Long> {

    @Query("select o from OLD_Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<OLD_Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from OLD_Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}

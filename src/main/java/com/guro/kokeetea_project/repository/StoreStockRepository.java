package com.guro.kokeetea_project.repository;

import com.guro.kokeetea_project.entity.StoreStock;
import com.guro.kokeetea_project.entity.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreStockRepository extends JpaRepository<StoreStock, Long>{
    @Query("select ss from StoreStock ss where ss.isValid = true")
    List<StoreStock> listStoreStock(Pageable pageable);

    @Query("select ss from StoreStock ss where ss.isValid = true")
    List<StoreStock> listStoreStock();

    @Query("select count(ss) from StoreStock ss where ss.isValid = true")
    Long countStoreStock();
}

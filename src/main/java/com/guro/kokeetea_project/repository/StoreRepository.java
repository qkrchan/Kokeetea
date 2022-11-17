package com.guro.kokeetea_project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.guro.kokeetea_project.entity.Store;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select s from Store s where s.isValid = true")
    List<Store> listStore(Pageable pageable);

    @Query("select s from Store s where s.isValid = true")
    List<Store> listStore();

    @Query("select count(s) from Store s where s.isValid = true")
    Long countStore();
}

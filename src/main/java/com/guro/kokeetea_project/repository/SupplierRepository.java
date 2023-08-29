package com.guro.kokeetea_project.repository;

import com.guro.kokeetea_project.entity.Category;
import com.guro.kokeetea_project.entity.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("select su from Supplier su where su.isValid = true")
    List<Supplier> listSupplier(Pageable pageable);

    @Query("select su from Supplier su where su.isValid = true")
    List<Supplier> listSupplier();

    @Query("select count(su) from Supplier su where su.isValid = true")
    Long countSupplier();

}

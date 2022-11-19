package com.guro.kokeetea_project.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.guro.kokeetea_project.dto.StatOfRequestByMonth;
import com.guro.kokeetea_project.dto.StatOfRequestByMonthIngredient;
import com.guro.kokeetea_project.dto.StatOfRequestByMonthStore;
import com.guro.kokeetea_project.entity.Ingredient;
import com.guro.kokeetea_project.entity.Store;
import com.guro.kokeetea_project.entity.Warehouse;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guro.kokeetea_project.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select r from Request r order by r.date desc")
    List<Request> listRequest(Pageable pageable);

    @Query("select r from Request r order by r.id desc")
    List<Request> listRequestId(Pageable pageable);

    @Query("select r from Request r order by r.ingredient desc")
    List<Request> listRequestIngredient(Pageable pageable);

    @Query("select r from Request r order by r.amount desc")
    List<Request> listRequestAmount(Pageable pageable);

    @Query("select r from Request r order by r.store desc")
    List<Request> listRequestStore(Pageable pageable);

    @Query("select r from Request r order by r.warehouse desc")
    List<Request> listRequestWarehouse(Pageable pageable);

    @Query("select r from Request r order by r.status desc")
    List<Request> listRequestStatus(Pageable pageable);

    @Query("select r from Request r where r.store.email = :email order by r.date desc")
    List<Request> mylistRequest(@Param("email") String email, Pageable pageable);

    @Query("select count(r) from Request r ")
    Long countRequest();

    @Query("select new com.guro.kokeetea_project.dto.StatOfRequestByMonth(year(min(r.date)), month(min(r.date)), count(r)) from Request r where r.date >= :start and r.date < :end group by year(r.date)*100+month(r.date) order by year(r.date)*100+month(r.date) asc")
    List<StatOfRequestByMonth> countByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);   // start: start of last year (inclusive) // end: end of last year (exclusive)

    @Query("select new com.guro.kokeetea_project.dto.StatOfRequestByMonthIngredient(r.ingredient.id, r.ingredient.name, count(r)) from Request r where r.date >= :start and r.date < :end group by r.ingredient order by r.ingredient.name asc")
    List<StatOfRequestByMonthIngredient> countByMonthIngredient(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end); // start: start of last month (inclusive) // end: end of last month (exclusive)

    @Query("select new com.guro.kokeetea_project.dto.StatOfRequestByMonthStore(r.store.id, r.store.name, count(r)) from Request r where r.date >= :start and r.date < :end group by r.store order by r.store.name asc")
    List<StatOfRequestByMonthStore> countByMonthStore(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end); // start: start of last month (inclusive) // end: end of last month (exclusive)

    @Query("select count(r) from Request r where r.store.email = :email")
    Long mycountRequest(@Param("email") String email);

    List<Request> findByIngredient(Ingredient ingredient);

    void deleteByIngredient(Ingredient ingredient);

    void deleteByStore(Store store);

    void deleteByWarehouse(Warehouse warehouse);
}

package com.guro.kokeetea_project.repository.mainPage;

import com.guro.kokeetea_project.entity.MainAvg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MainAvgRepository extends JpaRepository<MainAvg,Long> {

    @Query(value = "SELECT round(avg(amount)) AS ingredient_avg " +
            " FROM request GROUP BY ingredient_id",nativeQuery = true)
    List<Integer> listRequest();

    @Query(value = "SELECT count(amount) AS ingredient_count" +
            " FROM request GROUP BY ingredient_id",nativeQuery = true)
    List<Integer> ingredindCount();

    @Query(value = "SELECT " +
            "  count(case when date between '2022-10-01' and '2022-10-31' then 1 end) AS '10월',count(case when date between '2022-09-01' and '2022-09-30' then 1 end) AS '9월'," +
            "  count(case when date between '2022-08-01' and '2022-08-31' then 1 end) AS '8월',count(case when date between '2022-07-01' and '2022-07-31' then 1 end) AS '7월'," +
            "  count(case when date between '2022-06-01' and '2022-06-30' then 1 end) AS '6월',count(case when date between '2022-05-01' and '2022-05-31' then 1 end) AS '5월'" +
            "FROM " +
            " request",nativeQuery = true)
    Object monthCount();

    @Query(value = "SELECT round(avg(amount)) AS store_avg " +
            " FROM request GROUP BY store_id",nativeQuery = true)
    List<Integer> storeCount();
}

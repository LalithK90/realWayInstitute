package lk.real_way_institute.asset.batch.dao;



import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.batch.entity.enums.ClassDay;
import lk.real_way_institute.asset.batch.entity.enums.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface BatchDao extends JpaRepository< Batch, Integer > {

  Batch findFirstByOrderByIdDesc();

  Batch findByName(String name);


  Batch findByNameAndStartAtIsBetweenAndEndAtIsBetween(String name, LocalDate startAt, LocalDate endAt, LocalDate startAt1, LocalDate endAt1);
}

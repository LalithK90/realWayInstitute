package lk.real_way_institute.asset.instalment_date.dao;


import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lk.real_way_institute.asset.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InstalmentDateDao extends JpaRepository< InstalmentDate, Integer> {

  InstalmentDate findFirstByOrderByIdDesc();

  List<InstalmentDate> findByDateBefore(LocalDate localDate);
}

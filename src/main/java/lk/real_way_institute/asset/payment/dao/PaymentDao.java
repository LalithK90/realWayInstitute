package lk.real_way_institute.asset.payment.dao;


import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lk.real_way_institute.asset.payment.entity.Payment;
import lk.real_way_institute.asset.payment.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository< Payment, Integer> {

  Payment findFirstByOrderByIdDesc();



  List< Payment> findByCreatedAtIsBetween(LocalDateTime startAt, LocalDateTime endAt);


  Payment findByInstalmentDateAndBatchStudent(InstalmentDate instalmentDate, BatchStudent batchStudent);
}

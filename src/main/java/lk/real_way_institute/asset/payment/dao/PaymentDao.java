package lk.real_way_institute.asset.payment.dao;


import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.payment.entity.Payment;
import lk.real_way_institute.asset.payment.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Month;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer> {

  Payment findFirstByOrderByIdDesc();

  Payment findByBatchStudentAndMonth(BatchStudent batchStudent, Month month);

  Payment findByMonthAndBatchStudentAndPaymentStatus(BatchStudent batchStudent, Month month, PaymentStatus noPaid);
}

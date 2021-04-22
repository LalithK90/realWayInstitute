package lk.real_way_institute.asset.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lk.real_way_institute.asset.student.entity.Student;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentRemainder {
  private Student student;
  private String batchName;
  private BigDecimal amount;
  private LocalDate date;
  private String message;

}

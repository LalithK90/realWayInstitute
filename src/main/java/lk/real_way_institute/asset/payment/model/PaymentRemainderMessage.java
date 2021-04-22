package lk.real_way_institute.asset.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lk.real_way_institute.asset.student.entity.Student;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRemainderMessage {
  private List< PaymentRemainder > paymentRemainders;

}


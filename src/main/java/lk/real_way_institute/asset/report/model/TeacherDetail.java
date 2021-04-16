package lk.real_way_institute.asset.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetail {

private long paymentCount;
private BigDecimal totalPay;

private long areasCount;
private BigDecimal totalAreaPay;

}

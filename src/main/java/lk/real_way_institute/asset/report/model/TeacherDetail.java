package lk.real_way_institute.asset.report.model;

import lk.succes_student_management.asset.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetail {

private Teacher teacher;
private long paymentCount;
private BigDecimal totalPay;

private long areasCount;
private BigDecimal totalAreaPay;

}

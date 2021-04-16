package lk.real_way_institute.asset.report.model;


import lk.real_way_institute.asset.batch.entity.Batch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InComeVsTeacher {
private Batch batch;
private BigDecimal totalPaymentAmount;
private BigDecimal areasAmount;
}

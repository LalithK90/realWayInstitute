package lk.real_way_institute.asset.payment.model;

import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchStudentInstalmentDate {
  private BatchStudent batchStudent;
  private InstalmentDate instalmentDate;
}

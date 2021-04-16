package lk.real_way_institute.asset.batch_exam.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.succes_student_management.asset.batch.entity.Batch;
import lk.succes_student_management.asset.batch_exam.entity.enums.ExamStatus;
import lk.succes_student_management.asset.batch_student_exam_result.entity.BatchStudentExamResult;
import lk.succes_student_management.asset.common_asset.model.enums.LiveDead;
import lk.succes_student_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "BatchExam" )
public class BatchExam extends AuditEntity {

  @Column( unique = true )
  private String code;

  private String remark;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @Enumerated(EnumType.STRING)
  private ExamStatus examStatus;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime startAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime endAt;

  @ManyToOne
  private Batch batch;

  @OneToMany(mappedBy = "batchExam")
  private List< BatchStudentExamResult > batchStudentExamResults;
}

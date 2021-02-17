package lk.real_way_institute.asset.time_table.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.hall.entity.Hall;
import lk.real_way_institute.asset.time_table_student_attendence.entity.TimeTableStudentAttendance;
import lk.real_way_institute.util.audit.AuditEntity;
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
@JsonFilter( "TimeTable" )
public class TimeTable extends AuditEntity {
@Column(unique = true)
  private String code;

  private String lesson;

  private String remark;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime startAt;

  @DateTimeFormat( pattern = "yyyy-MM-dd'T'HH:mm" )
  private LocalDateTime endAt;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne
  private Batch batch;

  @ManyToOne
  private Hall hall;

  @OneToMany(mappedBy = "timeTable",cascade = CascadeType.PERSIST)
  private List< TimeTableStudentAttendance > timeTableStudentAttendances;
  }

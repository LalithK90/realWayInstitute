package lk.real_way_institute.asset.batch.entity;


import com.fasterxml.jackson.annotation.JsonFilter;

import lk.real_way_institute.asset.batch_exam.entity.BatchExam;
import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.employee.entity.Employee;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lk.real_way_institute.asset.time_table.entity.TimeTable;
import lk.real_way_institute.util.audit.AuditEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonFilter( "Batch" )
public class Batch extends AuditEntity {

  @Column( unique = true )
  private String code;

  @Column( unique = true )
  private String name;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @DateTimeFormat( pattern = "HH:mm" )
  private LocalTime startAt;

  @DateTimeFormat( pattern = "HH:mm" )
  private LocalTime endAt;

  private int installmentCount;

  @ManyToOne( cascade = {CascadeType.MERGE, CascadeType.PERSIST} )
  private InstalmentDate instalmentDate;

  @OneToMany( mappedBy = "batch" )
  private List< BatchStudent > batchStudents;

  @OneToMany( mappedBy = "batch" )
  private List< TimeTable > timeTables;

  @OneToMany( mappedBy = "batch" )
  private List< BatchExam > batchExams;

  @ManyToMany
  @JoinTable( name = "batch_employee",
      joinColumns = @JoinColumn( name = "batch_id" ),
      inverseJoinColumns = @JoinColumn( name = "employee_id" ) )
  private List< Employee > employees;


  @Transient
  private int count;

  @Transient
  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate date;
}

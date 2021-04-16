package lk.real_way_institute.asset.instalment_date.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "InstalmentDate" )
public class InstalmentDate extends AuditEntity {

  @Column(unique = true)
  private String code;

  @Enumerated( EnumType.STRING)
  private LiveDead liveDead;

  @DateTimeFormat( pattern = "yyyy-MM-dd" )
  private LocalDate date;

  private BigDecimal amount;


}

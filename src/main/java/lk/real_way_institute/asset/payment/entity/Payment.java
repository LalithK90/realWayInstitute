package lk.real_way_institute.asset.payment.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.succes_student_management.asset.batch_student.entity.BatchStudent;
import lk.succes_student_management.asset.common_asset.model.enums.LiveDead;
import lk.succes_student_management.asset.payment.entity.enums.PaymentStatus;
import lk.succes_student_management.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Payment" )
public class Payment extends AuditEntity {

    @Column(unique = true)
    private String code;

    private BigDecimal amount;

    @DateTimeFormat( pattern = "yyyy" )
    private Year year;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private Month month;

    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

    @ManyToOne
    private BatchStudent batchStudent;


}

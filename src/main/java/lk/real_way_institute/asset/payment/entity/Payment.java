package lk.real_way_institute.asset.payment.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lk.real_way_institute.asset.payment.entity.enums.PaymentStatus;
import lk.real_way_institute.util.audit.AuditEntity;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
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


    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

    @ManyToOne
    private BatchStudent batchStudent;

    @ManyToOne
    private InstalmentDate instalmentDate;


}

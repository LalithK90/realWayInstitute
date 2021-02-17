package lk.real_way_institute.asset.hall.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.hall.entity.enums.HallCondition;
import lk.real_way_institute.asset.time_table.entity.TimeTable;
import lk.real_way_institute.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Hall")
public class Hall extends AuditEntity {

    @Enumerated( EnumType.STRING )
    private LiveDead liveDead;

    private String name;

    private String number;

    @Enumerated( EnumType.STRING )
    private HallCondition hallCondition;

    private String seatCount;

    private String hallLocation;

    @OneToMany( mappedBy = "hall" )
    private List< TimeTable > timeTables;

}

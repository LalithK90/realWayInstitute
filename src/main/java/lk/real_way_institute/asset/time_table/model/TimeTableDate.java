package lk.real_way_institute.asset.time_table.model;

import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.time_table.entity.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableDate {
private LocalDate date;
private List<TimeTable> timeTables;
private boolean status;
}

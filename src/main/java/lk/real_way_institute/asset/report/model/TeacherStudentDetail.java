package lk.real_way_institute.asset.report.model;


import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.teacher.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherStudentDetail {
private Teacher teacher;
private List< BatchStudent > batchStudents;

}

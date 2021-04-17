package lk.real_way_institute.asset.student.dao;


import lk.real_way_institute.asset.batch.entity.enums.Grade;
import lk.real_way_institute.asset.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository< Student, Integer > {

    Student findFirstByOrderByIdDesc();

}

package lk.real_way_institute.asset.teacher.dao;

import lk.real_way_institute.asset.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherDao extends JpaRepository<Teacher, Integer> {
    Teacher findFirstByOrderByIdDesc();
}

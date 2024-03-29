package lk.real_way_institute.asset.user_management.dao;


import lk.real_way_institute.asset.employee.entity.Employee;
import lk.real_way_institute.asset.student.entity.Student;
import lk.real_way_institute.asset.user_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository< User, Integer > {

  @Query( value = "select id from User where employee_id=?1", nativeQuery = true )
  Integer findByEmployeeId(@Param( "employee_id" ) Integer id);

  @Query( "select id from User where username=?1" )
  Integer findUserIdByUserName(String userName);

  User findByUsername(String name);

  User findByEmployee(Employee employee);


  User findByStudent(Student student);

}

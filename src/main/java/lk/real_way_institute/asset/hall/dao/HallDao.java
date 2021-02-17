package lk.real_way_institute.asset.hall.dao;


import lk.real_way_institute.asset.hall.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HallDao extends JpaRepository<Hall, Integer> {

}

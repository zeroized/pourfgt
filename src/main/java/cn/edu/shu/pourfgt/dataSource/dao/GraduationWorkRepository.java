package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationWork;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationWorkRepository extends CrudRepository<GraduationWork, Long> {
    List<GraduationWork> findByYear(int year);

    List<GraduationWork> findByYearAndSemester(int year, int semester);
}

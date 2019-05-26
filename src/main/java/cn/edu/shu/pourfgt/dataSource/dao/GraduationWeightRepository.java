package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationWeight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationWeightRepository extends CrudRepository<GraduationWeight, Long> {
    List<GraduationWeight> findByTeacherIdAndYearAndSemester(String teacherId, int year, int semester);

    GraduationWeight findById(long id);
}

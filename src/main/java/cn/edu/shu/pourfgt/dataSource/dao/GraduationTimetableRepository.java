package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationTimetable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationTimetableRepository extends CrudRepository<GraduationTimetable, Long> {
    List<GraduationTimetable> findByYearAndSemester(int year, int semester);
}

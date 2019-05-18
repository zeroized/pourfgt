package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseRegularGradeRecord;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRegularGradeRecordRepository extends CrudRepository<CourseRegularGradeRecord, Long> {
    List<CourseRegularGradeRecord> findByAttachedIdAndStudentId(long attachedId, long studentId);
}

package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseStudentRepository extends CrudRepository<CourseStudent, Long> {
    List<CourseStudent> findByAttachedId(long attachedId);

    List<CourseStudent> findByStudentId(long studentId);
}

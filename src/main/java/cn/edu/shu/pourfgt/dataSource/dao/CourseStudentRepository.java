package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.data.repository.CrudRepository;

public interface CourseStudentRepository extends CrudRepository<CourseStudent, Long> {
}

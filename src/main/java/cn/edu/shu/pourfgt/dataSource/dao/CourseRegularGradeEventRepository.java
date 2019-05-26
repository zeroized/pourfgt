package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseRegularGradeEvent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRegularGradeEventRepository extends CrudRepository<CourseRegularGradeEvent, Long> {
    List<CourseRegularGradeEvent> findByAttachedId(long attachedId);

    CourseRegularGradeEvent findById(long id);
}

package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseInfoRepository extends CrudRepository<CourseInfo, Long> {
    List<CourseInfo> findByTeacherId(String teacherId);
}

package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CoursePost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoursePostRepository extends CrudRepository<CoursePost, Long> {
    List<CoursePost> findByAttachedId(long attachedId);

    List<CoursePost> findByTypeAndWeek(int type, int week);

    CoursePost findById(long id);
}

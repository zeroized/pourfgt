package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CoursePost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CoursePostRepository extends CrudRepository<CoursePost, Long> {
    List<CoursePost> findByAttachedId(long attachedId);

    List<CoursePost> findByAttachedIdAndTypeAndWeek(long attachedId, int type, int week);

    List<CoursePost> findByAttachedIdAndType(long attachedId, int type);

    CoursePost findById(long id);
}

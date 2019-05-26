package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseDiscussion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseDiscussionRepository extends CrudRepository<CourseDiscussion, Long> {
    List<CourseDiscussion> findByAttachedIdAndDiscussionId(long attachedId, long discussionId);

    List<CourseDiscussion> findByAttachedId(long attachedId);
}

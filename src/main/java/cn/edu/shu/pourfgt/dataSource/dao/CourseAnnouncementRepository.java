package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseAnnouncement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseAnnouncementRepository extends CrudRepository<CourseAnnouncement, Long> {
    List<CourseAnnouncement> findByAttachedId(long attachedId);

    List<CourseAnnouncement> findByTypeAndWeek(int type, int week);

    CourseAnnouncement findById(long id);
}

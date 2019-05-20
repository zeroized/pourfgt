package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.CourseStudentMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseStudentMessageRepository extends CrudRepository<CourseStudentMessage, Long> {
    CourseStudentMessage findById(long id);

    List<CourseStudentMessage> findByAttachedId(long attachedId);

    List<CourseStudentMessage> findByAttachedIdAndType(long attachedId, int type);

    List<CourseStudentMessage> findByAttachedIdAndTypeAndHomeworkWeek(long attachedId, int type, int homeworkWeek);

    List<CourseStudentMessage> findByAttachedIdAndHomeworkWeek(long attachedId, int homeworkWeek);
}

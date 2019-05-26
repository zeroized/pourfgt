package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationStudentMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationStudentMessageRepository extends CrudRepository<GraduationStudentMessage, Long> {
    List<GraduationStudentMessage> findByYearAndSemester(int year, int semester);

    List<GraduationStudentMessage> findByTeacherIdAndYearAndSemester(String teacherId, int year, int semester);

    GraduationStudentMessage findById(long id);
}

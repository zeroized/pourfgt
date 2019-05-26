package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationPost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationPostRepository extends CrudRepository<GraduationPost, Long> {
    List<GraduationPost> findByYearAndSemester(int year, int semester);

    List<GraduationPost> findByTeacherIdAndYearAndSemester(String teacherId, int year, int semester);

    GraduationPost findById(long id);
}

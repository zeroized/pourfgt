package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.GraduationStudentSubmission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GraduationStudentSubmissionRepository extends CrudRepository<GraduationStudentSubmission, Long> {
    GraduationStudentSubmission findFirstByYearAndSemesterAndStudentId(int year, int semester, long studentId);

    List<GraduationStudentSubmission> findByYearAndSemesterAndStudentId(int year, int semester, long studentId);

}

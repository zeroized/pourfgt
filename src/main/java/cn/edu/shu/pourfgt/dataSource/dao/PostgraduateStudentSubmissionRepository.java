package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateStudentSubmission;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgraduateStudentSubmissionRepository extends CrudRepository<PostgraduateStudentSubmission, Long> {
    List<PostgraduateStudentSubmission> findByStudentId(long studentId);

    PostgraduateStudentSubmission findById(long id);
}

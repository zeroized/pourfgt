package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateStudent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgraduateStudentRepository extends CrudRepository<PostgraduateStudent, Long> {
    List<PostgraduateStudent> findByActive(boolean active);

    PostgraduateStudent findById(long id);
}

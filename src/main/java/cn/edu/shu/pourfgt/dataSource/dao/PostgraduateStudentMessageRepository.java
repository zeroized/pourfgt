package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateStudentMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgraduateStudentMessageRepository extends CrudRepository<PostgraduateStudentMessage, Long> {
    PostgraduateStudentMessage findById(long id);

    List<PostgraduateStudentMessage> findAll();

}

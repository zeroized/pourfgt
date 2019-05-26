package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.PostgraduatePost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgraduatePostRepository extends CrudRepository<PostgraduatePost, Long> {
    List<PostgraduatePost> findByYearAndType(int year, int type);

    List<PostgraduatePost> findAll();

    List<PostgraduatePost> findByTeacherId(String teacherId);

    PostgraduatePost findById(long id);
}

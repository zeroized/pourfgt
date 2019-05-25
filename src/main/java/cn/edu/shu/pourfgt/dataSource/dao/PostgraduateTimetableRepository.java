package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateTimetable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgraduateTimetableRepository extends CrudRepository<PostgraduateTimetable, Long> {
    List<PostgraduateTimetable> findByForYearAndForType(int forYear, int forType);
}

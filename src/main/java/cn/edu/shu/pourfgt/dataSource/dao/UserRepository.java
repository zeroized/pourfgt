package cn.edu.shu.pourfgt.dataSource.dao;

import cn.edu.shu.pourfgt.dataSource.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUserId(String userId);
}

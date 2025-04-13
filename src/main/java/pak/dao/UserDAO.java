package pak.dao;

import pak.entity.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();
    void save(User user);
    User findOne(Long id);
    void update(User user);
    void delete(Long id);

}
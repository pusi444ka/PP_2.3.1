package pak.dao;

import pak.entity.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsers();
    void save(User user);
    User getUserById(Long id);
    void update(User user);
    void delete(Long id);

}
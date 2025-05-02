package pak.service;

import pak.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    void createUser(User user);

    User findUserById(Long id);

    void updateUser(User user);

    void removeUserById(Long id);
}
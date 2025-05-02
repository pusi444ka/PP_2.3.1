package pak.service;

import org.springframework.beans.factory.annotation.Autowired;
import pak.dao.UserDAO;
import pak.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

    private final UserDAO userDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
    @Transactional
    @Override
    public void createUser(User user) {
        userDao.createUser(user);
    }
    @Transactional(readOnly = true)
    @Override
    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }
    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
    @Transactional
    @Override
    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }
}
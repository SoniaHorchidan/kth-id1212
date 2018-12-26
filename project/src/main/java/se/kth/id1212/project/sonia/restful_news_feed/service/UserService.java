package se.kth.id1212.project.sonia.restful_news_feed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;
import se.kth.id1212.project.sonia.restful_news_feed.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveUser(User user) throws ServiceError {
        boolean usernameExists = userRepository.existsByName(user.getName());
        if(usernameExists)
            throw new ServiceError();
        userRepository.save(user);
    }

    public User login(User user) throws Exception {
        User foundUser = userRepository.getUserByNameAndPassword(user.getName(), user.getPassword());
        if(foundUser == null)
            throw new ServiceError();
        return foundUser;
    }

    public User getUserById(long id) {
        return userRepository.findOne(id);
    }

    public User getUserByName(String username) {
        return userRepository.getUserByName(username);
    }
}

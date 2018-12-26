package se.kth.id1212.project.sonia.restful_news_feed.repository;

import org.springframework.data.repository.CrudRepository;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User getUserByNameAndPassword(String username, String password);

    User getUserByName(String name);

    boolean existsByName(String username);
}
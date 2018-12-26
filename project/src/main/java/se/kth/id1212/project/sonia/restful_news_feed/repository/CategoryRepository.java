package se.kth.id1212.project.sonia.restful_news_feed.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import se.kth.id1212.project.sonia.restful_news_feed.entity.Category;

@Service
public interface CategoryRepository extends CrudRepository<Category, Long> {
}

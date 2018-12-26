package se.kth.id1212.project.sonia.restful_news_feed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.id1212.project.sonia.restful_news_feed.entity.Category;
import se.kth.id1212.project.sonia.restful_news_feed.repository.CategoryRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Iterable<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(long categId) {
        return categoryRepository.findOne(categId);
    }
}

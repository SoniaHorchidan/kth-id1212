package se.kth.id1212.project.sonia.restful_news_feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import se.kth.id1212.project.sonia.restful_news_feed.entity.Category;
import se.kth.id1212.project.sonia.restful_news_feed.entity.NewsEntry;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;
import se.kth.id1212.project.sonia.restful_news_feed.repository.CategoryRepository;
import se.kth.id1212.project.sonia.restful_news_feed.repository.NewsEntryRepository;
import se.kth.id1212.project.sonia.restful_news_feed.repository.UserRepository;

import java.util.ArrayList;

@Component
public class NewsFeedApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsEntryRepository newsEntryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        Category category1 = new Category("Tech");
        Category category2 = new Category("Beauty");
        Category category3 = new Category("Lifestyle");
        Category category4 = new Category("Nature");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);

        User user1 = new User("user1", "pass");
        User user2 = new User("user2", "pass");
        ArrayList<Category> pref = new ArrayList<>();
        pref.add(category1);
        userRepository.save(user1);
        user1.setPreferredCategories(pref);
        userRepository.save(user1);
        userRepository.save(user2);

        newsEntryRepository.save(new NewsEntry(user1, "NEWS1", "Content of news 1", true, category1));
        newsEntryRepository.save(new NewsEntry(user2, "NEWS2","Content of news 2", false, category2));
        newsEntryRepository.save(new NewsEntry(user2, "NEWS3","Content of news 3", false, category3));
        newsEntryRepository.save(new NewsEntry(user1, "NEWS4","Content of news 4", true, category4));
    }
}

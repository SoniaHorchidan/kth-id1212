package se.kth.id1212.project.sonia.restful_news_feed.repository;


import org.springframework.data.repository.CrudRepository;
import se.kth.id1212.project.sonia.restful_news_feed.entity.Category;
import se.kth.id1212.project.sonia.restful_news_feed.entity.NewsEntry;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface NewsEntryRepository extends CrudRepository<NewsEntry, Long> {
    List<NewsEntry> getAllByCategory(Category category);
    List<NewsEntry> getAllByOwner(User owner);

    default List<NewsEntry> getAllUsersFavourites(User user) {
        ArrayList<NewsEntry> entries = new ArrayList<NewsEntry>();
        for(Category category: user.getPreferredCategories())
            entries.addAll(getAllByCategory(category));
        return entries;
    }

}


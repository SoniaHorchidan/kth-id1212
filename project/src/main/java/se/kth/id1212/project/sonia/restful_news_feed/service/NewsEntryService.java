package se.kth.id1212.project.sonia.restful_news_feed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.id1212.project.sonia.restful_news_feed.entity.NewsEntry;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;
import se.kth.id1212.project.sonia.restful_news_feed.repository.NewsEntryRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class NewsEntryService {
    @Autowired
    private NewsEntryRepository newsEntryRepository;

    public Iterable<NewsEntry> listAllEntries() {
        return newsEntryRepository.findAll();
    }

    public NewsEntry findEntryById(long id) {
        return newsEntryRepository.findOne(id);
    }

    public void saveEntry(NewsEntry newsEntry) {
        newsEntryRepository.save(newsEntry);
    }

    public Iterable<NewsEntry> listUsersFavouriteEntries(User loggedInUser) {
        return newsEntryRepository.getAllUsersFavourites(loggedInUser);
    }

    public void deleteEntry(long id) {
        newsEntryRepository.delete(id);
    }

    public Iterable<NewsEntry> listUsersEntries(User loggedInUser) {
        return newsEntryRepository.getAllByOwner(loggedInUser);
    }

    public Iterable<NewsEntry> findAll() {
        return newsEntryRepository.findAll();
    }
}

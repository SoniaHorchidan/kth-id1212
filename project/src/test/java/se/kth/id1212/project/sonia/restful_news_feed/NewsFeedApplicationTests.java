package se.kth.id1212.project.sonia.restful_news_feed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.id1212.project.sonia.restful_news_feed.entity.NewsEntry;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;
import se.kth.id1212.project.sonia.restful_news_feed.service.NewsEntryService;
import se.kth.id1212.project.sonia.restful_news_feed.service.UserService;

import java.util.ArrayList;

import static junit.framework.TestCase.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NewsFeedApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private NewsEntryService newsEntryService;

	@Test
    public void indexPageLoad() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void validLogin() throws Exception {
        this.mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "user1")
                .param("password", "pass"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("username", "user1"));
    }

    @Test
    public void invalidLogin() throws Exception {
        this.mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "user1")
                .param("password", "password"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void validRegistration() throws Exception {
        this.mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "user3")
                .param("password", "pass")
                .param("categs", "1", "2"))
                .andExpect(status().isCreated());

        User newUser = userService.getUserByName("user3");
        assertNotNull(newUser);
    }

    @Test
    public void invalidRegistrationBecauseOfNameConflict() throws Exception {
        this.mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "user1")
                .param("password", "pass")
                .param("categs", "1", "2"))
                .andExpect(status().isConflict());
    }

    @Test
    public void invalidRegistrationBecauseOfMissingFields() throws Exception {
        this.mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "user4")
                .param("password", "pass"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void editSuccessfully() throws Exception {
        this.mvc.perform(post("/edit/1")
                .sessionAttr("username", "user1")
                .sessionAttr("userId", 1l)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "My news entry")
                .param("content", "The content of the entry"))
                .andExpect(status().isOk());

        NewsEntry newsEntry = newsEntryService.findEntryById(1);
        assertEquals("My news entry", newsEntry.getTitle());
        assertEquals("The content of the entry", newsEntry.getContent());
    }

    @Test
    public void editUnsuccessfullyBecauseOfMissingFields() throws Exception {
	    // TODO change expected status
        this.mvc.perform(post("/edit/1")
                .sessionAttr("username", "user1")
                .sessionAttr("userId", 1l)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "My new news entry"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void insertSuccessfully() throws Exception {
	    int initialNumOfEntries = ((ArrayList<NewsEntry>) newsEntryService.findAll()).size();

        this.mvc.perform(post("/insert")
                .sessionAttr("username", "user1")
                .sessionAttr("userId", 1l)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "New entry")
                .param("content", "The content of the entry")
                .param("writePermission", "true")
                .param("categs", "2"))
                .andExpect(status().isCreated());

        int finalNumOfEntries = ((ArrayList<NewsEntry>) newsEntryService.findAll()).size();
        assertEquals(initialNumOfEntries + 1, finalNumOfEntries);
    }

    @Test
    public void insertUnsuccessfullyBecauseOfMissingFields() throws Exception {
        int initialNumOfEntries = ((ArrayList<NewsEntry>) newsEntryService.findAll()).size();

        this.mvc.perform(post("/insert")
                .sessionAttr("username", "user1")
                .sessionAttr("userId", 1l)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "New entry")
                .param("content", "The content of the entry")
                .param("writePermission", "true"))
                .andExpect(status().isBadRequest());

        int finalNumOfEntries = ((ArrayList<NewsEntry>) newsEntryService.findAll()).size();
        assertEquals(initialNumOfEntries, finalNumOfEntries);
    }

    @Test
    public void delete() throws Exception {
        this.mvc.perform(get("/delete/2")
                .sessionAttr("username", "user1")
                .sessionAttr("userId", 1l))
                .andExpect(status().isOk());

        NewsEntry newsEntry = newsEntryService.findEntryById(2);
        assertNull(newsEntry);
    }

    @Test
    public void accessUnavailablePath() throws Exception {
        this.mvc.perform(get("/unavailable"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void logout() throws Exception {
        this.mvc.perform(get("/logout"))
                .andExpect(status().isOk());
    }

}

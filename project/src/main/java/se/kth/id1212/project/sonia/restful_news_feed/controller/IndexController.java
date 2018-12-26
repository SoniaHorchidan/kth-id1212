package se.kth.id1212.project.sonia.restful_news_feed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se.kth.id1212.project.sonia.restful_news_feed.controller.exceptions.*;
import se.kth.id1212.project.sonia.restful_news_feed.entity.Category;
import se.kth.id1212.project.sonia.restful_news_feed.entity.NewsEntry;
import se.kth.id1212.project.sonia.restful_news_feed.entity.User;
import se.kth.id1212.project.sonia.restful_news_feed.service.CategoryService;
import se.kth.id1212.project.sonia.restful_news_feed.service.NewsEntryService;
import se.kth.id1212.project.sonia.restful_news_feed.service.ServiceError;
import se.kth.id1212.project.sonia.restful_news_feed.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
@SessionAttributes({"username", "userId"})
public class IndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private NewsEntryService newsEntryService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String goToIndex(Model model) {
        return goToFeedIfLoggedIn(model);
    }

    @GetMapping("/index")
    public String goToIndex2(Model model) {
        return goToFeedIfLoggedIn(model);
    }

    @GetMapping("/news-feed")
    public String goToFeed(Model model) {
        return goToFeedIfLoggedIn(model);
    }

    private String goToFeedIfLoggedIn(Model model) {
        if (userIsLoggedIn(model))
            return redirectTo("news-feed", model);
        else
            return redirectTo("index", model);
    }

    private String redirectTo(String page, Model model) {
        if (page.equals("news-feed")) {
            User user = getLoggedInUserFromModel(model);
            //model.addAttribute("loggedInUser", userId);
            model.addAttribute("entries", newsEntryService.listUsersFavouriteEntries(user));
            model.addAttribute("myEntries", newsEntryService.listUsersEntries(user));
            model.addAttribute("favCategories", user.getPreferredCategories());
        } else
            model.addAttribute("entries", newsEntryService.listAllEntries());
        return page;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid User user, BindingResult result, Model model) {
        User loggedInUser = null;
        try {
            loggedInUser = userService.login(user);
        } catch (Exception ex) {
            throw new UnauthorizedException();
        }

        model.addAttribute("username", loggedInUser.getName());
        model.addAttribute("userId", loggedInUser.getId());

        return redirectTo("news-feed", model);
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        model.asMap().clear();
        return redirectTo("index", model);
    }

    @GetMapping("/register")
    public String showRegisterForm(User user, Model model) {
        model.addAttribute("categoryList", categoryService.listCategories());
        return "register";
    }

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String registerUser(@Valid User user, @RequestParam(value = "categs") long[] categories,
                               BindingResult result, Model model) {

        ArrayList<Category> preferredCategories = new ArrayList<>();
        for (long categId : categories) {
            Category category = categoryService.findCategoryById(categId);
            preferredCategories.add(category);
        }
        user.setPreferredCategories(preferredCategories);
        try {
            userService.saveUser(user);
        } catch (ServiceError serviceError) {
            throw new RegistrationFailedException();
        }
        return redirectTo("index", model);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        if (!userIsLoggedIn(model))
            return redirectTo("index", model);
        NewsEntry newsEntry = newsEntryService.findEntryById(id);
        if (newsEntry == null)
            throw new ResourceNotFoundException();

        model.addAttribute("newsEntry", newsEntry);
        return "update-entry";
    }

    @PostMapping("/edit/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateEntry(@PathVariable("id") long id, @Valid NewsEntry newsEntry,
                              BindingResult result, Model model) {
        if (!userIsLoggedIn(model))
            return redirectTo("index", model);
//        if (result.hasErrors()) {
//            newsEntry.setId(id);
//            return "update-entry";
//        }
        User loggedInUser = getLoggedInUserFromModel(model);
        NewsEntry originalNewsEntry = newsEntryService.findEntryById(id);
        newsEntry.setOwner(originalNewsEntry.getOwner());
        newsEntry.setWritePermission(originalNewsEntry.isWritePermission());
        newsEntry.setCategory(originalNewsEntry.getCategory());
        newsEntry.setLastUpdatedBy(loggedInUser);
        try{
            newsEntryService.saveEntry(newsEntry);
        } catch (Exception ex) {
            throw new UpdateFailedException();
        }

        return redirectTo("news-feed", model);
    }

    @GetMapping("/insert")
    public String showInsertForm(NewsEntry newsEntry, Model model) {
        if (!userIsLoggedIn(model))
            return redirectTo("index", model);
        model.addAttribute("categoryList", categoryService.listCategories());
        return "insert-entry";
    }

    @PostMapping("/insert")
    @ResponseStatus(value = HttpStatus.CREATED)
    public String insertEntry(@Valid NewsEntry newsEntry, @RequestParam(value = "categs") long categId,
                              BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "insert-entry";
//        }
        User loggedInUser = getLoggedInUserFromModel(model);
        Category category = categoryService.findCategoryById(categId);
        newsEntry.setCategory(category);
        newsEntry.setOwner(loggedInUser);
        newsEntry.setLastUpdatedBy(loggedInUser);
        try{
            newsEntryService.saveEntry(newsEntry);
        } catch (Exception ex){
            throw new InsertFailedException();
        }

        return redirectTo("news-feed", model);
    }

    @GetMapping("/delete/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteUser(@PathVariable("id") long id, Model model) {
        if (!userIsLoggedIn(model))
            return redirectTo("index", model);
        NewsEntry entry = newsEntryService.findEntryById(id);
        if (entry == null)
            throw new ResourceNotFoundException();
        try{
            newsEntryService.deleteEntry(id);
        } catch(Exception ex){
            throw new DeleteFailedException();
        }

        model.addAttribute("users", newsEntryService.listAllEntries());
        return redirectTo("news-feed", model);
    }

    @RequestMapping(value = {"{path:(?!resources|static).*$}", "{path:(?!resources|static)*$}/**"}, headers = "Accept=text/html")
    public void matchUnknownPaths() {
        throw new ResourceNotFoundException();
    }

    private User getLoggedInUserFromModel(Model model) {
        long userId = (long) model.asMap().get("userId");
        return userService.getUserById(userId);
    }

    private boolean userIsLoggedIn(Model model) {
        return model.containsAttribute("username");
    }
}
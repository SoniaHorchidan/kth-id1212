package se.kth.id1212.project.sonia.restful_news_feed.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    private String name;

    private String password;

    @OneToMany(mappedBy="owner")
    private List<NewsEntry> newsEntries;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Category> preferredCategories = new ArrayList<>();

    public User(){}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String email) {
        this.password = email;
    }

    public List<Category> getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(ArrayList<Category> preferredCategories) {
        this.preferredCategories.addAll(preferredCategories);
    }
}
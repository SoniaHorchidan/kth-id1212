package se.kth.id1212.project.sonia.restful_news_feed.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class NewsEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    private User owner;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @NotBlank(message = "Title is mandatory")
    private String title;

    private boolean writePermission = false;

    @OneToOne
    @JoinColumn(name="lastUpdatedBy_id")
    private User lastUpdatedBy;

    @OneToOne
    @JoinColumn(name="category_id")
    @NotNull(message = "Category is required")
    private Category category;

    public NewsEntry(User owner, String title, String content, boolean writePermission, Category category) {
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.writePermission = writePermission;
        this.lastUpdatedBy = owner;
        this.category = category;
    }

    public NewsEntry(){}

    public long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getContent() {
        return content;
    }

    public boolean isWritePermission() {
        return writePermission;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "NewsEntry{" +
                "id=" + id +
                ", owner=" + owner +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", writePermission=" + writePermission +
                ", lastUpdatedBy=" + lastUpdatedBy +
                ", category=" + category +
                '}';
    }
}

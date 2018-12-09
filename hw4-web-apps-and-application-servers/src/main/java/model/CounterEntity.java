package model;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(
                name = "getCounter",
                query = "SELECT ce FROM CounterEntity ce"
        )
})

@Entity
@Table(name = "COUNTER", schema = "APP")
public class CounterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String performedAt;

    public CounterEntity(String performedAt) {
        this.performedAt = performedAt;
    }

    public CounterEntity() {
    }

    @Basic
    @Column(name = "PERFORMEDAT")
    public String getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(String performedAt) {
        this.performedAt = performedAt;
    }

    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounterEntity that = (CounterEntity) o;

        if (id != that.id) return false;
        if (performedAt != null ? !performedAt.equals(that.performedAt) : that.performedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = performedAt != null ? performedAt.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }
}

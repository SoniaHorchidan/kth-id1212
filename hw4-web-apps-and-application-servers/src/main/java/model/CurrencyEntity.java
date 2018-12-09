package model;

import javax.persistence.*;


@NamedQueries({
        @NamedQuery(
                name = "getAllCurrencies",
                query = "SELECT OBJECT(ce) FROM CurrencyEntity ce"
        ),
        @NamedQuery(
                name = "findCurrency",
                query = "SELECT OBJECT(ce) FROM CurrencyEntity ce WHERE ce.name = :currencyName"
        )
})


@Entity
@Table(name = "CURRENCY", schema = "APP")
public class CurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    public CurrencyEntity(String name) {
        this.name = name;
    }

    public CurrencyEntity() {
    }

    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyEntity that = (CurrencyEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}

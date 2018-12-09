package model;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "findConversionRate",
                query = "SELECT OBJECT(ce) FROM ConverterEntity ce WHERE ce.fromcurrency = :fromCurrency AND ce.tocurrenty = :toCurrency"
        )
})

@Entity
@Table(name = "CONVERTER", schema = "APP")
public class ConverterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Integer fromcurrency;
    private Integer tocurrenty;
    private Float rate;

    public ConverterEntity(Integer fromcurrency, Integer tocurrenty, Float rate) {
        this.fromcurrency = fromcurrency;
        this.tocurrenty = tocurrenty;
        this.rate = rate;
    }

    public ConverterEntity() {
    }

    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FROMCURRENCY")
    public Integer getFromcurrency() {
        return fromcurrency;
    }

    public void setFromcurrency(Integer fromcurrency) {
        this.fromcurrency = fromcurrency;
    }

    @Basic
    @Column(name = "TOCURRENTY")
    public Integer getTocurrenty() {
        return tocurrenty;
    }

    public void setTocurrenty(Integer tocurrenty) {
        this.tocurrenty = tocurrenty;
    }

    @Basic
    @Column(name = "RATE")
    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConverterEntity that = (ConverterEntity) o;

        if (id != that.id) return false;
        if (fromcurrency != null ? !fromcurrency.equals(that.fromcurrency) : that.fromcurrency != null) return false;
        if (tocurrenty != null ? !tocurrenty.equals(that.tocurrenty) : that.tocurrenty != null) return false;
        if (rate != null ? !rate.equals(that.rate) : that.rate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fromcurrency != null ? fromcurrency.hashCode() : 0);
        result = 31 * result + (tocurrenty != null ? tocurrenty.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        return result;
    }
}

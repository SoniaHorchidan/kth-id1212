package integration;

import model.ConverterEntity;
import model.CounterEntity;
import model.CurrencyEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class ConverterDAO {
    @PersistenceContext(unitName = "converterPU")
    private EntityManager entityManager;

    public List<CurrencyEntity> getAllCurrencies() {
        return entityManager.createNamedQuery("getAllCurrencies", CurrencyEntity.class).getResultList();
    }

    public float getConversionRate(CurrencyEntity fromCurrency, CurrencyEntity toCurrency) throws EntityException {
        try {
            ConverterEntity converterEntity = getConversion(fromCurrency, toCurrency);
            return converterEntity.getRate();
        } catch (Exception ex) {
            throw new EntityException("The conversion could not be performed. Please contact an admin.");
        }
    }

    public ConverterEntity getConversion(CurrencyEntity fromCurrency, CurrencyEntity toCurrency) {
        try {
            return entityManager.createNamedQuery("findConversionRate", ConverterEntity.class)
                                .setParameter("fromCurrency", fromCurrency.getId())
                                .setParameter("toCurrency", toCurrency.getId())
                                .getSingleResult();
        } catch (Exception ex) {
            return null;
        }

    }

    public CurrencyEntity findCurrency(int id) {
        return entityManager.find(CurrencyEntity.class, id);
    }

    public CurrencyEntity findCurrency(String name) {
        try {
            return entityManager.createNamedQuery("findCurrency", CurrencyEntity.class)
                                .setParameter("currencyName", name)
                                .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }


    public CurrencyEntity addNewCurrency(String currencyName) {
        if (!currencyExists(currencyName)) {
            CurrencyEntity newEntity = new CurrencyEntity(currencyName);
            entityManager.persist(newEntity);
            return newEntity;
        } else {
            CurrencyEntity oldEntity = findCurrency(currencyName);
            return oldEntity;
        }
    }

    private boolean currencyExists(String name) {
        CurrencyEntity result = findCurrency(name);
        if (result == null)
            return false;
        return true;
    }

    public ConverterEntity findConversion(String fromCurrency, String toCurrency) {
        CurrencyEntity from = findCurrency(fromCurrency);
        CurrencyEntity to = findCurrency(toCurrency);
        return getConversion(from, to);
    }

    public void saveConversion(String currentDate) {
        entityManager.persist(new CounterEntity(currentDate));
    }

    public void addNewConversion(CurrencyEntity fromCurrency, CurrencyEntity toCurrency, float rate) throws EntityException {
        ConverterEntity conversion = findConversion(fromCurrency.getName(), toCurrency.getName());
        if (conversion != null) {
            throw new EntityException("Conversion between " + fromCurrency.getName() + " and " + toCurrency.getName() + " already exists.");
        } else {
            ConverterEntity newConversion = new ConverterEntity(fromCurrency.getId(), toCurrency.getId(), rate);
            entityManager.persist(newConversion);
        }
    }

    public int getCounter() {
        List<CounterEntity> conversionsPerformedSoFar = entityManager.createNamedQuery("getCounter", CounterEntity.class)
                                                                     .getResultList();
        return conversionsPerformedSoFar.size();
    }
}

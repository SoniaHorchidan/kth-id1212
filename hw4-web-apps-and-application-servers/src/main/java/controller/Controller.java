package controller;

import integration.ConverterDAO;
import integration.EntityException;
import model.CurrencyEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Controller {
    @EJB
    ConverterDAO converterDAO;

    public List<CurrencyEntity> getAllCurrencies() {
        return converterDAO.getAllCurrencies();
    }

    public float convert(int from, int to, float amount) throws EntityException {
        CurrencyEntity fromCurrency = converterDAO.findCurrency(from);
        CurrencyEntity toCurrency = converterDAO.findCurrency(to);
        float conversionRate = converterDAO.getConversionRate(fromCurrency, toCurrency);
        reportConversion();
        return amount * conversionRate;
    }

    private void reportConversion() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        converterDAO.saveConversion(currentDate);
    }

    public void addNewConversion(String fromCurrency, String toCurrency, float rate) throws EntityException {
        CurrencyEntity newFrom = converterDAO.addNewCurrency(fromCurrency);
        CurrencyEntity newTo = converterDAO.addNewCurrency(toCurrency);
        performAddingConversion(rate, newFrom, newTo);
    }

    private void performAddingConversion(float rate, CurrencyEntity newFrom, CurrencyEntity newTo) throws EntityException {
        converterDAO.addNewConversion(newFrom, newTo, rate);
        converterDAO.addNewConversion(newTo, newFrom, 1 / rate);
    }

    public int getCounter() {
        return converterDAO.getCounter();
    }
}

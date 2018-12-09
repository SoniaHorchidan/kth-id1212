package view;

import controller.Controller;
import integration.EntityException;
import model.CurrencyEntity;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("converterManager")
@SessionScoped
public class ConverterManager implements Serializable {
    @EJB
    private Controller controller;
    private List<CurrencyEntity> currencies;
    private int fromCurrency;
    private int toCurrency;
    private float valueToConvert;
    private float result = 0;
    private Exception exception;
    private String fromNewCurrency;
    private String toNewCurrency;
    private float newRate;
    private int counter;

    @PostConstruct
    public void init() {
        currencies = controller.getAllCurrencies();
        counter = controller.getCounter();
    }

    private void handleException(Exception ex) {
        ex.printStackTrace(System.err);
        exception = ex;
    }

    public boolean getSuccess() {
        currencies = controller.getAllCurrencies();
        return exception == null;
    }

    public Exception getException() {
        return exception;
    }

    public List<CurrencyEntity> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyEntity> currencies) {
        this.currencies = currencies;
    }

    public int getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(int fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public int getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(int toCurrency) {
        this.toCurrency = toCurrency;
    }

    public float getValueToConvert() {
        return valueToConvert;
    }

    public void setValueToConvert(float valueToConvert) {
        this.valueToConvert = valueToConvert;
    }

    public void convert() {
        exception = null;
        try {
            result = controller.convert(fromCurrency, toCurrency, valueToConvert);
            counter = controller.getCounter();
        } catch (EntityException ex) {
            handleException(ex);
        }
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public String getFromNewCurrency() {
        return fromNewCurrency;
    }

    public void setFromNewCurrency(String fromNewCurrency) {
        this.fromNewCurrency = fromNewCurrency;
    }

    public String getToNewCurrency() {
        return toNewCurrency;
    }

    public void setToNewCurrency(String toNewCurrency) {
        this.toNewCurrency = toNewCurrency;
    }

    public float getNewRate() {
        return newRate;
    }

    public void setNewRate(float newRate) {
        this.newRate = newRate;
    }

    public void add() {
        exception = null;
        try {
            controller.addNewConversion(fromNewCurrency, toNewCurrency, newRate);
            currencies = controller.getAllCurrencies();
        } catch (EntityException e) {
            handleException(e);
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}

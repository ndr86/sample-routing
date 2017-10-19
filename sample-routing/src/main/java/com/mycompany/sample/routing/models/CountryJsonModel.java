package com.mycompany.sample.routing.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author a.grimaldi
 */
public class CountryJsonModel implements Serializable {

    private String countryName;
    private List<String> currenciesNames;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<String> getCurrenciesNames() {
        return currenciesNames;
    }

    public void setCurrenciesNames(List<String> currenciesNames) {
        this.currenciesNames = currenciesNames;
    }

}

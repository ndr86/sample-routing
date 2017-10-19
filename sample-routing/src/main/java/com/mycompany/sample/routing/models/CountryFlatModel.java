package com.mycompany.sample.routing.models;

import java.io.Serializable;

/**
 *
 * @author a.grimaldi
 */
public class CountryFlatModel implements Serializable {

    private String countryName;
    private String currenciesNames;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrenciesNames() {
        return currenciesNames;
    }

    public void setCurrenciesNames(String currenciesNames) {
        this.currenciesNames = currenciesNames;
    }

}

package com.mycompany.sample.routing.controllers;

import com.mycompany.sample.routing.models.Country;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author a.grimaldi
 */
@Controller
public class CountryController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getCountries() {

        List<Country> countries = retrieveData(false);

        ModelAndView model = new ModelAndView("index");
        model.addObject("countries", countries);
        return model;
    }

    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public String getPagedCountries() {
        return "redirect:paging/0";
    }

    @RequestMapping(value = "/paging/{page}", method = RequestMethod.GET)
    public ModelAndView getPagedCountries(@PathVariable int page) {

        List<Country> countries = retrieveData(true);

        int start = page * 5;
        int end = (page + 1) * 5;

        ModelAndView model = new ModelAndView("paging");

        if (page >= 0 && end <= countries.size()) {
            model.addObject("countries", countries.subList(start, Math.min(end, countries.size())));
        }
        model.addObject("page", page);
        model.addObject("prevActive", page != 0);
        model.addObject("nextActive", end < countries.size());

        return model;
    }

    @RequestMapping(value = "/secured", method = RequestMethod.GET)
    public String getSecuredCountries() {
        return "redirect:/";
    }

    private List<Country> retrieveData(boolean sortable) {
        String requestURL = "https://restcountries.eu/rest/v2/all";
        HttpURLConnection connection = null;
        int resCode = 0;
        List<Country> countryList = new ArrayList<>();

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            // Install the all-trusting trust manager
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL url = new URL(requestURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            resCode = connection.getResponseCode();
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        switch (resCode) {
            case 200:
                // Read response
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = bufferedReader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    inputStreamReader.close();
                    bufferedReader.close();

                    JSONArray countries = new JSONArray(response.toString());

                    for (int i = 0; i < countries.length(); i++) {

                        JSONObject countryObject = countries.getJSONObject(i);

                        if (countryObject != null
                                && !countryObject.isNull("name")) {

                            String countryName = String.valueOf(countryObject.get("name"));

                            if (countryName != null) {

                                Country country = new Country();

                                country.setCountryName(countryName);

                                JSONArray currencies = countryObject.getJSONArray("currencies");

                                String currenciesName = "";
                                if (currencies != null) {

                                    for (int k = 0; k < currencies.length(); k++) {
                                        JSONObject currencyObject = currencies.getJSONObject(k);

                                        if (currencyObject != null
                                                && !currencyObject.isNull("name")) {
                                            String currencyName = String.valueOf(currencyObject.get("name"));

                                            if (currencyName != null) {
                                                currenciesName = currenciesName.concat(currencyName);
                                                currenciesName = currenciesName.concat(", ");
                                            }
                                        }
                                    }
                                }
                                country.setCurrenciesNames(currenciesName.substring(0, currenciesName.length() - 2));

                                countryList.add(country);
                            }
                        }
                    }
                } catch (IOException | JSONException ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }
                break;
            default:
                break;
        }

        if (sortable) {
            Collections.sort(countryList, new CustomComparator());
        }

        return countryList;
    }

    public class CustomComparator implements Comparator<Country>, Serializable {

        @Override
        public int compare(Country c1, Country c2) {
            return c1.getCountryName().compareTo(c2.getCountryName());
        }
    }

}

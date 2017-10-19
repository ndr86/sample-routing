package com.mycompany.sample.routing.api;

import com.mycompany.sample.routing.models.CountryJsonModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author a.grimaldi
 */
@RestController
public class CountryApi {

    @RequestMapping(value = "/api",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CountryJsonModel>> getAllCountries() {
        List<CountryJsonModel> list = retrieveData(false);

        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private List<CountryJsonModel> retrieveData(boolean sortable) {
        String requestURL = "https://restcountries.eu/rest/v2/all";
        HttpURLConnection connection = null;
        int resCode = 0;
        List<CountryJsonModel> countryList = new ArrayList<>();

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
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
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException ex) {
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

                                CountryJsonModel country = new CountryJsonModel();

                                country.setCountryName(countryName);

                                JSONArray currencies = countryObject.getJSONArray("currencies");

                                List<String> currenciesNames = new ArrayList<>();
                                if (currencies != null) {

                                    for (int k = 0; k < currencies.length(); k++) {
                                        JSONObject currencyObject = currencies.getJSONObject(k);

                                        if (currencyObject != null
                                                && !currencyObject.isNull("name")) {
                                            String currencyName = String.valueOf(currencyObject.get("name"));

                                            if (currencyName != null) {
                                                currenciesNames.add(currencyName);
                                            }
                                        }
                                    }
                                }
                                country.setCurrenciesNames(currenciesNames);

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

    public class CustomComparator implements Comparator<CountryJsonModel>, Serializable {

        @Override
        public int compare(CountryJsonModel c1, CountryJsonModel c2) {
            return c1.getCountryName().compareTo(c2.getCountryName());
        }
    }

}

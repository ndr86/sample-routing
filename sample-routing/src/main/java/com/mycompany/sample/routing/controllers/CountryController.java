package com.mycompany.sample.routing.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
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

        // -----
        String requestURL = "https://restcountries.eu/rest/v2/all";
        HttpURLConnection connection = null;
        int resCode = 0;

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

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        try {
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
                if (connection != null) {
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
//                        System.out.println(conuntries);

                        for (int i = 0; i < countries.length(); i++) {
                            JSONObject countryObject = countries.getJSONObject(i);

                            if (countryObject != null
                                    && countryObject.get("name") != null) {
                                String name = String.valueOf(countryObject.get("name"));
                                System.out.print(name + ", ");

                                JSONArray currencies = countryObject.getJSONArray("currencies");

                                if (currencies != null) {
                                    for (int k = 0; k < currencies.length(); k++) {
//                                        System.out.print(k);
                                        JSONObject currencyObject = currencies.getJSONObject(k);

                                        if (currencyObject != null
                                                && currencyObject.get("name") != null) {
                                            name = String.valueOf(currencyObject.get("name"));
                                            System.out.print(name);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException | JSONException ex) {
                        System.out.println("Exception: " + ex.getMessage());
                    }
                }
                break;
            default:
                break;
        }

        //// ----------------------------------------
        //// ----------------------------------------
        //// ----------------------------------------
        //// ----------------------------------------
        //// ----------------------------------------
        //// ----------------------------------------
        //// ----------------------------------------
        List<String> list = getList();

        ModelAndView model = new ModelAndView("index");
        model.addObject("lists", list);
        return model;

    }

    private List<String> getList() {

        List<String> list = new ArrayList<>();
        list.add("List A");
        list.add("List B");
        list.add("List C");
        list.add("List D");
        list.add("List 1");
        list.add("List 2");
        list.add("List 3");

        return list;

    }

}

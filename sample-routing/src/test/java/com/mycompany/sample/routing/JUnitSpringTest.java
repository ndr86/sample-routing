package com.mycompany.sample.routing;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.mycompany.sample.routing.config.AppConfig;
import com.mycompany.sample.routing.controllers.CountryController;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author a.grimaldi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class JUnitSpringTest {

    @Autowired
    private CountryController countryController;

    @Test
    public void testCountryController() {
        assertEquals(
                "class com.mycompany.sample.routing.controllers.CountryController",
                this.countryController.getClass().toString());
    }

    @Test
    public void testGetCountries() {

        ModelAndView modelAndView = countryController.getCountries();

        Map<String, Object> map = modelAndView.getModel();

        assertNotNull(map.get("countries"));
    }

    @Test
    public void testGetPagedCountries() {

        ModelAndView modelAndView = countryController.getPagedCountries(0);

        Map<String, Object> map = modelAndView.getModel();

        assertEquals(map.get("prevActive"), false);
        assertEquals(((List) map.get("countries")).size(), 5);
    }
}

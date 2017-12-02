package it.sample.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.sample.model.Country;
import it.sample.model.Currency;

/**
 * Test for {@link RestCountriesClientBasic}
 *
 * @author ccardone
 */
@RunWith(MockitoJUnitRunner.class)
public class RestCountriesClientBasicTest {

   @Mock
   private RestTemplate restTemplate;

   @Mock
   private ResponseEntity<Country[]> responseEntity;

   // class to test
   @InjectMocks
   private RestCountriesClientBasic restCountriesClientBasic;

   @Test
   public void testGetAllWithNameAndCurrency() {

      // setup
      Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Country[].class))).thenReturn(responseEntity);
      Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
      Mockito.when(responseEntity.getBody()).thenReturn(new Country[]{
            new Country("Italy", Collections.singletonList(new Currency("EUR", "euro", "€")))
      });

      // call to method to test
      List<Country> allWithNameAndCurrency = restCountriesClientBasic.getAllWithNameAndCurrency();

      // verify
      Mockito.verify(restTemplate, Mockito.only()).getForEntity(Mockito.eq("https://restcountries.eu/rest/v2/all?fields=name;currencies"), Mockito.eq(Country[].class));

      // assert
      Assert.assertNotNull("Response must be not null", allWithNameAndCurrency);
      Assert.assertEquals("Must be only one country", 1, allWithNameAndCurrency.size());

      Country responseCountry = allWithNameAndCurrency.get(0);
      Assert.assertEquals("Italy", responseCountry.getName());

      List<Currency> responseCountryCurrencies = responseCountry.getCurrencies();
      Assert.assertNotNull("Currency list must be not null", responseCountryCurrencies);

      Assert.assertEquals("Must be only one currency", 1, responseCountryCurrencies.size());
      Currency responseCurrency = responseCountryCurrencies.get(0);
      Assert.assertEquals("Wrong currency code", "EUR", responseCurrency.getCode());
      Assert.assertEquals("Wrong currency name", "euro", responseCurrency.getName());
      Assert.assertEquals("Wrong currency symbol", "€", responseCurrency.getSymbol());

   }

   @Test(expected = Exception.class)
   public void testGetAllWithNameAndCurrencyIOException() {

      // setup
      Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Country[].class))).thenThrow(new IOException());

      // call to method to test
      restCountriesClientBasic.getAllWithNameAndCurrency();
   }

   @Test(expected = Exception.class)
   public void testGetAllWithNameAndCurrencyStatusCodeNotOk() {

      // setup
      Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Country[].class))).thenReturn(responseEntity);
      Mockito.when(responseEntity.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

      // call to method to test
      restCountriesClientBasic.getAllWithNameAndCurrency();
   }
}

package it.sample.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import it.sample.model.CountriesServiceResponse;
import it.sample.model.Country;
import it.sample.model.Currency;

/**
 * Test for @{@link CountriesEndpoint}
 *
 * @author ccardone
 */
@RunWith(MockitoJUnitRunner.class)
public class CountriesEndpointTest {

   @Mock
   private RestCountriesClient restCountriesClient;

   @Mock
   private Message<MultiValueMap<String, String>> inMessage;

   private LinkedMultiValueMap<String, String> parametersMap;

   private List<Country> sampleCountryList;

   private Country italy;
   private Country albania;
   private Country usa;

   // class to test
   @InjectMocks
   private CountriesEndpoint countriesEndpoint;

   @Before
   public void setUp() {
      Mockito.when(inMessage.getHeaders()).thenReturn(new MessageHeaders(Collections.emptyMap()));
      parametersMap = new LinkedMultiValueMap<>();
      Mockito.when(inMessage.getPayload()).thenReturn(parametersMap);

      sampleCountryList = new ArrayList<>();

      italy = new Country("Italy", Collections.singletonList(new Currency("EUR", "euro", "€")));
      albania = new Country("Albania", Collections.singletonList(new Currency("ALL", "Albanian lek", "L")));
      usa = new Country("Usa", Collections.singletonList(new Currency("USD", "United State Dollar", "$")));

      sampleCountryList.add(italy);
      sampleCountryList.add(albania);
      sampleCountryList.add(usa);
   }

   /**
    * page 1 -> italy
    * page 2 -> albania
    * page 3 -> usa
    */
   @Test
   public void testGetFirstPageSize1() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(sampleCountryList);
      parametersMap.put("pageSize", Collections.singletonList("1"));

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      List<Country> countries = checkResponse(outMessage, 1, 3, 0, 1);
      Country country = countries.get(0);

      Assert.assertEquals("Wrong country", italy, country);

      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   /**
    * page 1 -> italy,albania
    * page 2 -> usa
    */
   @Test
   public void testGetFirstPageSize2() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(sampleCountryList);
      parametersMap.put("pageSize", Collections.singletonList("2"));

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      List<Country> countries = checkResponse(outMessage, 2, 3, 0, 2);

      Assert.assertEquals("Wrong country", italy, countries.get(0));
      Assert.assertEquals("Wrong country", albania, countries.get(1));

      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   /**
    * page 1 -> italy
    * page 2 -> albania
    * page 3 -> usa
    */
   @Test
   public void testGetSecondPageSize1() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(sampleCountryList);
      parametersMap.put("pageSize", Collections.singletonList("1"));
      parametersMap.put("page", Collections.singletonList("1"));

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      List<Country> countries = checkResponse(outMessage, 1, 3, 1, 1);

      Assert.assertEquals("Wrong country", albania, countries.get(0));

      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   /**
    * page 1 -> italy, albania
    * page 2 -> usa
    */
   @Test
   public void testGetLastPageSize2() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(sampleCountryList);
      parametersMap.put("pageSize", Collections.singletonList("2"));
      parametersMap.put("page", Collections.singletonList("1"));

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      List<Country> countries = checkResponse(outMessage, 2, 3, 1, 1);

      Assert.assertEquals("Wrong country", usa, countries.get(0));

      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   /**
    * page 1 -> italy,albania,usa
    */
   @Test
   public void testGetAll() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(sampleCountryList);
      parametersMap.put("pageSize", Collections.singletonList("10"));

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      List<Country> countries = checkResponse(outMessage, 10, 3, 0, 3);

      Assert.assertEquals("Wrong country", italy, countries.get(0));
      Assert.assertEquals("Wrong country", albania, countries.get(1));
      Assert.assertEquals("Wrong country", usa, countries.get(2));

      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   @Test
   public void testGetException() {

      // setup
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenThrow(new RuntimeException());

      // call method to test
      Message<CountriesServiceResponse> outMessage = countriesEndpoint.get(inMessage);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      Assert.assertNotNull("Out message must be not null", outMessage);
      Assert.assertNotNull("paylod must be not null", outMessage.getPayload());
      CountriesServiceResponse countriesServiceResponse = outMessage.getPayload();

      Assert.assertEquals("Wrong current page", 0, countriesServiceResponse.getCurrentPage());
      Assert.assertEquals("Wrong items for page", 0, countriesServiceResponse.getPageSize());
      Assert.assertEquals("Wrong total items", 0, countriesServiceResponse.getTotalItems());
      Assert.assertTrue("paylod must be empty", countriesServiceResponse.getCountries().isEmpty());
      Assert.assertEquals(500, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   private List<Country> checkResponse(Message<CountriesServiceResponse> outMessage, int pageSize, int totalItems, int currentPage, int listSize) {
      Assert.assertNotNull("Out message must be not null", outMessage);
      CountriesServiceResponse countriesServiceResponse = outMessage.getPayload();

      Assert.assertEquals("Wrong current page", currentPage, countriesServiceResponse.getCurrentPage());
      Assert.assertEquals("Wrong items for page", pageSize, countriesServiceResponse.getPageSize());
      Assert.assertEquals("Wrong total items", totalItems, countriesServiceResponse.getTotalItems());

      List<Country> countries = countriesServiceResponse.getCountries();

      Assert.assertNotNull("Countries must not null", countries);

      Assert.assertEquals("Wrong countries size", listSize, countries.size());
      return countries;
   }
}

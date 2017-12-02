package it.sample.service;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;

import it.sample.model.Country;

/**
 * Test for @{@link CountriesEndpoint}
 *
 * @author ccardone
 */
@RunWith(MockitoJUnitRunner.class)
public class CountriesEndpointTest {

   @Mock
   private RestCountriesClient restCountriesClient;

   // class to test
   @InjectMocks
   private CountriesEndpoint countriesEndpoint;

   @Test
   public void testGet() throws Exception {

      // setup
      List<Country> countryList = Collections.emptyList();
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenReturn(countryList);

      // call method to test
      Message<List<Country>> outMessage = countriesEndpoint.get(null);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      Assert.assertNotNull("Out message must be not null", outMessage);
      Assert.assertSame("Out list must be the same of list returned by service", countryList, outMessage.getPayload());
      Assert.assertEquals(200, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }

   @Test
   public void testGetException() throws Exception {

      // setup
      List<Country> countryList = Collections.emptyList();
      Mockito.when(restCountriesClient.getAllWithNameAndCurrency()).thenThrow(new RuntimeException());

      // call method to test
      Message<List<Country>> outMessage = countriesEndpoint.get(null);

      // verify
      Mockito.verify(restCountriesClient, Mockito.only()).getAllWithNameAndCurrency();

      // assert
      Assert.assertNotNull("Out message must be not null", outMessage);
      Assert.assertNotNull("paylod must be not null", outMessage.getPayload());
      Assert.assertTrue("paylod must be empty", outMessage.getPayload().isEmpty());
      Assert.assertEquals(500, outMessage.getHeaders().get(HttpHeaders.STATUS_CODE));
   }
}

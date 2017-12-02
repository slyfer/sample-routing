package it.sample.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.sample.model.Country;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements only the get all with name and currency
 *
 * @author ccardone
 */
@Service
@Slf4j
public class RestCountriesClientBasic implements RestCountriesClient {

   @Autowired
   @Setter
   private RestTemplate restTemplate;

   // TODO EXTRACT TO PROPERTIES
   private static final String REST_COUNTRIES_REST_ENDPOINT = "https://restcountries.eu/rest/v2";

   @Override
   // @Cacheable TODO ADD CACHE
   public List<Country> getAllWithNameAndCurrency() {

      log.debug("Calling {} for all countries", REST_COUNTRIES_REST_ENDPOINT);
      ResponseEntity<Country[]> responseEntity = restTemplate.getForEntity(REST_COUNTRIES_REST_ENDPOINT + "/all?fields=name;currencies", Country[].class);

      HttpStatus statusCode = responseEntity.getStatusCode();
      log.debug("Response status code is {}", statusCode);
      if (HttpStatus.OK != statusCode) {
         // TODO WE MUST DEFINE A CUSTOM EXCEPTION
         throw new IllegalStateException("Response status code is " + statusCode);
      }

      return Collections.unmodifiableList(Arrays.asList(responseEntity.getBody()));
   }
}

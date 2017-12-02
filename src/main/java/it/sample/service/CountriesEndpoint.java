package it.sample.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import it.sample.model.Country;
import lombok.extern.slf4j.Slf4j;

/**
 * Internal endpoint
 *
 * @author ccardone
 */
@Component("countriesEndpoint")
@Slf4j
public class CountriesEndpoint {

   @Autowired
   private RestCountriesClient restCountriesClient;

   // TODO ADD AUTHENTICATION AND PAGINATION
   public Message<List<Country>> get(Message<?> msg) {

      log.trace("Countries Endpoint");

      List<Country> allWithNameAndCurrency;
      int statusCode = 200;

      try {
         allWithNameAndCurrency = restCountriesClient.getAllWithNameAndCurrency();

      } catch (Exception e) {
         log.error("Unexpected error calling rest countries" + e.getMessage(), e);
         statusCode = 500;
         allWithNameAndCurrency = Collections.emptyList();
      }

      Map<String, Object> headers = new HashMap<>();
      headers.put(HttpHeaders.STATUS_CODE, statusCode);
      Message<List<Country>> message = new GenericMessage<>(allWithNameAndCurrency, headers);
      return message;
   }

}



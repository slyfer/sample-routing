package it.sample.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import it.sample.model.CountriesServiceResponse;
import it.sample.model.Country;
import lombok.extern.slf4j.Slf4j;

/**
 * Internal endpoint
 *
 * @author ccardone
 */
@Service("countriesEndpoint")
@Slf4j
public class CountriesEndpoint {

   private static final String DEFAULT_PAGE_SIZE = "10";

   @Autowired
   private RestCountriesClient restCountriesClient;

   @Secured("ROLE_REST_HTTP_USER")
   public Message<CountriesServiceResponse> get(Message<MultiValueMap<String, String>> msg) {
      int statusCode = 200;
      CountriesServiceResponse countriesServiceResponse;

      try {
         log.trace("Countries Endpoint");

         if (log.isTraceEnabled()) {
            msg.getHeaders().forEach((k, v) -> log.trace("{} -> {}", k, v));
         }

         MultiValueMap<String, String> parameters = msg.getPayload();

         if (log.isDebugEnabled()) {
            parameters.forEach((k, v) -> log.debug("{} -> {}", k, v));
         }

         Map<String, String> singleValueMap = parameters.toSingleValueMap();

         int pageSize = Integer.parseInt(singleValueMap.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
         int currentPage = Integer.parseInt(singleValueMap.getOrDefault("page", "0"));

         List<Country> allWithNameAndCurrency = restCountriesClient.getAllWithNameAndCurrency();

         countriesServiceResponse = getCountriesServiceResponse(pageSize, currentPage, allWithNameAndCurrency);
      } catch (Exception e) {
         log.error("Unexpected error " + e.getMessage(), e);
         statusCode = 500;

         // TODO ADD EXCEPTION HANLDER TO TRANSALTE EXCEPTION INTO HUMAN READABLE MESSAGES
         countriesServiceResponse = new CountriesServiceResponse(0, 0, 0, Collections.emptyList(), e.getMessage());
      }

      Map<String, Object> headers = new HashMap<>();
      headers.put(HttpHeaders.STATUS_CODE, statusCode);

      return new GenericMessage<>(countriesServiceResponse, headers);
   }

   private CountriesServiceResponse getCountriesServiceResponse(int pageSize, int currentPage, List<Country> allWithNameAndCurrency) {
      CountriesServiceResponse countriesServiceResponse;
      int totalItems = allWithNameAndCurrency.size();
      int fromIndex = currentPage * pageSize;
      int toIndex = fromIndex + pageSize > totalItems ? totalItems : fromIndex + pageSize;

      List<Country> subList = allWithNameAndCurrency.subList(fromIndex, toIndex);

      countriesServiceResponse = new CountriesServiceResponse(totalItems, currentPage, pageSize, subList, "OK");
      return countriesServiceResponse;
   }

}



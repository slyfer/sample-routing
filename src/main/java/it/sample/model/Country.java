package it.sample.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represent a subset of a country take from ws https://restcountries.eu/rest/v2/all
 *
 * @author ccardone
 */
@ToString
public class Country {

   @Getter
   @Setter
   private String country;

   @Getter
   @Setter
   private List<Currency> currencies;
}

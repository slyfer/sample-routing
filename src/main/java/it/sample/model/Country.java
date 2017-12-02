package it.sample.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represent a subset of a country take from ws https://restcountries.eu/rest/v2/all
 *
 * @author ccardone
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Country {

   @Getter
   @Setter
   private String name;

   @Getter
   @Setter
   private List<Currency> currencies;
}

package it.sample.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a subset of a country take from ws https://restcountries.eu/rest/v2/all
 *
 * @author ccardone
 */
@AllArgsConstructor
@Data
public class Country {

   @Getter
   @Setter
   private String name;

   @Getter
   @Setter
   private List<Currency> currencies;
}

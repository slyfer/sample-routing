package it.sample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a currency take from ws https://restcountries.eu/rest/v2/all
 *
 * @author ccardone
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

   @Getter
   @Setter
   private String code;

   @Getter
   @Setter
   private String name;

   @Getter
   @Setter
   private String symbol;
}

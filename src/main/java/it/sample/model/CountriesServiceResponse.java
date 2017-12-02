package it.sample.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Wrap paginated web service response
 *
 * @author ccardone
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CountriesServiceResponse {


   @Getter
   @Setter
   private long totalItems;

   @Getter
   @Setter
   private long currentPage;

   @Getter
   @Setter
   private int pageSize;

   @Getter
   @Setter
   private List<Country> countries;

   @Getter
   @Setter
   private String statusMessage;
}

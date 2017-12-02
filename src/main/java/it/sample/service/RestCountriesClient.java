package it.sample.service;

import java.util.List;

import it.sample.model.Country;

/**
 * Basic rest client for https://restcountries.eu/
 *
 * @author ccardone
 */
public interface RestCountriesClient {

   List<Country> getAllWithNameAndCurrency();
}

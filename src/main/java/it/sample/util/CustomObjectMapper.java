package it.sample.util;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Custom object mapper for further customization or module registration
 *
 * @author ccardone
 */
@Component("customObjectMapper")
public class CustomObjectMapper extends ObjectMapper {


   @PostConstruct
   public void postConstruct() {
      this.enable(SerializationFeature.INDENT_OUTPUT);
   }
}

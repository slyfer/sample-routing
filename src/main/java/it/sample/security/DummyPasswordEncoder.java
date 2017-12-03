package it.sample.security;

import org.springframework.stereotype.Component;

/**
 * Dummy password encoder
 *
 * @author ccardone
 */
@Component("passwordEncoder")
public class DummyPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

   @Override
   public String encode(CharSequence charSequence) {
      // FIXME in real world we must use and ending algorithm, like sha-1
      return charSequence.toString();
   }

   @Override
   public boolean matches(CharSequence charSequence, String s) {
      return charSequence.toString().equals(s);
   }
}

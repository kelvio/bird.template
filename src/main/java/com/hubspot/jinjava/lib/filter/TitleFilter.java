package com.hubspot.jinjava.lib.filter;

import org.apache.commons.lang3.text.WordUtils;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;

/**
 * Return a titlecased version of the value. I.e. words will start with uppercase letters, all remaining characters are lowercase.
 * 
 * @author jstehler
 */
public class TitleFilter implements Filter {

  @Override
  public String getName() {
    return "title";
  }

  @Override
  public Object filter(Object var, JinjavaInterpreter interpreter, String... args) {
    if (var instanceof String) {
      String value = (String) var;
      return WordUtils.capitalize(value);
    }
    return var;
  }

}

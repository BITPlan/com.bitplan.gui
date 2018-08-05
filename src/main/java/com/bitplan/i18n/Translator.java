/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.gui open source project
 *
 * Copyright 2017 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Translation utility
 * 
 * @author wf
 *
 */
public class Translator {
  // prepare a LOGGER
  protected static Logger LOGGER = Logger
      .getLogger("com.bitplan.i18");

  public static String BUNDLE_NAME = "i18n";
  public static Locale[] SUPPORTED_LOCALES = { Locale.ENGLISH, Locale.GERMAN };

  private static ResourceBundle resourceBundle;
  private static MessageFormat formatter;
  // ignore errors - if set to false Exceptions will be thrown instead
  public static boolean lenient=true;

  /**
   * translate the given message with the given params
   * 
   * @param messageName
   * @param params
   * @return - the translated text
   */
  public static String translate(String messageName, Object... params) {
    if (resourceBundle==null) {
      LOGGER.log(Level.SEVERE,"I18n is not initialized "+messageName+" not translated");
      return messageName;
    }
    try {
      if (messageName==null) {
        String msg="Translate asked for null - returned as empty string";
        if (!lenient)
          throw new IllegalArgumentException(msg);
        LOGGER.log(Level.INFO, msg);
        return "";
      }
        
      String message = resourceBundle.getString(messageName);
      formatter.applyPattern(message);
      String i18n = formatter.format(params);
      return i18n;
    } catch (MissingResourceException mre) {
      String msg="I18n is not fully configured "+messageName+" not translated";
      if (!lenient)
        throw new IllegalArgumentException(msg);
      LOGGER.log(Level.WARNING,msg);
      return messageName;
    }
  }

  /**
   * translate the given message with the given parameters
   * 
   * @param messageName
   * @param param
   * @return - the translated message
   */
  public static String translate(String messageName, Object param) {
    return translate(messageName,new Object[] { param });
  }

  /**
   * translate the given message
   * 
   * @param messageName
   * @return - the translated message
   */
  public static String translate(String messageName) {
    return translate(messageName,new Object[] {});
  }

  /**
   * get the current locale from the resource Bundle
   * 
   * @return - the locale
   */
  public static Locale getCurrentLocale() {
    if (resourceBundle == null)
      return null;
    return resourceBundle.getLocale();
  }

  /**
   * initialize me
   * 
   * @return the resource Bundle for the given localName
   */
  public static ResourceBundle initialize(String bundleName,
      String localeName) {
    BUNDLE_NAME = bundleName;
    if (localeName == null)
      localeName = Locale.getDefault().getLanguage();
    Locale locale = new Locale(localeName);
    Locale.setDefault(locale);
    return loadBundle(locale);
  }

  /**
   * load the bundle
   * 
   * @param locale
   * @return - the resource bundle for the given locale
   */
  public static ResourceBundle loadBundle(Locale locale) {
    resourceBundle = ResourceBundle.getBundle("i18n/" + BUNDLE_NAME, locale);
    formatter = new MessageFormat("");
    formatter.setLocale(locale);
    return resourceBundle;
  }

  /**
   * get the current bundle
   * 
   * @return the resource bundle
   */
  public static ResourceBundle getBundle() {
    return resourceBundle;
  }
}

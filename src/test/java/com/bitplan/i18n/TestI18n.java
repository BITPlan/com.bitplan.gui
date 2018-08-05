/**
 *
 * This file is part of the https://github.com/BITPlan/can4eve open source project
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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bitplan.gui.App;
import com.bitplan.gui.ExceptionHelp;
import com.bitplan.gui.Form;
import com.bitplan.gui.Group;
import com.bitplan.gui.Menu;
import com.bitplan.gui.MenuItem;

/**
 * check the translation for an Application - each Application might want to implement an extension for this test
 * @author wf
 *
 */
public abstract class TestI18n {
  @BeforeClass
  public static void unsetLenient() {
    Translator.lenient=false;
  }
  @SuppressWarnings("rawtypes")
  public abstract Class getI18nClass();
  public abstract App getApp() throws Exception;
  public abstract String getI18nName();
  
  protected static boolean show = false;
  protected static boolean showError = true;
  String locales[] = { "en", "de" };
  
  /**
   * get the underscored version of the given identifier
   * 
   * @param identifier
   * @return - the underscored identifier
   */
  public static String asUnderScore(String identifier) {
    String underScoredIdentifier = identifier
        .replaceAll("(.)(\\p{Upper})", "$1_$2").toUpperCase();
    return underScoredIdentifier;
  }

  /**
   * check the given text
   * @param text
   * @return the number of errors
   */
  public int checkText(String text) {
    return checkText(text,"");
  }
  
  /**
   * check the given text whether it is available as a translate
   * 
   * @param text
   * @return 0 if translated 1 if not
   */
  public int checkText(String text, String hint) {
    String translated;
    int errors = 0;
    try {
      translated = I18n.get(text);
    } catch (Throwable th) {
      translated = "";
      if (show || showError) {
        System.out.println(text + "("+hint+")=" + translated);
      }
      errors++;
    }
    return errors;
  }

  /**
   * check the translation of the given menu
   * 
   * @param menu
   *          - the menu to check
   * @return - the number of errors
   */
  public int checkMenu(Menu menu) {
    int errors = 0;
    List<Menu> submenus = menu.getSubMenus();
    if (submenus.size() == 0)
      errors += checkText(menu.getId());
    for (Menu submenu : submenus) {
      errors += checkMenu(submenu);
    }
    for (MenuItem menuItem : menu.getMenuItems()) {
      errors += checkText(menuItem.getId());
    }
    return errors;
  }

  @Test
  public void testMenuTranslated() throws Exception {
    App app =getApp();
    int errors = 0;
    for (String locale : locales) {
      Translator.initialize(getI18nName(), locale);
      if (show)
        System.out.println("# locale " + locale);
      errors += checkMenu(app.getMainMenu());
    }
    assertEquals(0, errors);
  }

  /**
   * check the translations
   * 
   * @throws Exception
   * @throws IllegalArgumentException
   */
  @Test
  public void testi18nFieldsTranslated() throws Exception {
    int errors = 0;
    for (String locale : locales) {
      Translator.initialize(getI18nName(), locale);
      if (show)
        System.out.println("# locale " + locale);
      for (Field field : getI18nClass().getFields()) {
        String text = (String) field.get(null);
        errors += checkText(text);
      }
    }
    assertEquals(0, errors);
  }

  @Test
  public void testForms() throws Exception {
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      Translator.initialize(getI18nName(), locale);
      for (Group group:app.getGroups()) {
         errors+=checkText(group.getName(),group.getId()+"/"+group.getIcon());
         for (Form form:group.getForms()) {
           errors+=checkText(form.getTitle(),form.getId()+"/"+form.getIcon());
           for (com.bitplan.gui.Field field:form.getFields()) {
             errors+=checkText(form.getTitle()+"_"+field.getTitle());
           }
         }
      }
    }
    assertEquals(0, errors);
  }

  @Test
  public void textExceptionHelp() throws Exception {
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      Translator.initialize(getI18nName(), locale);
      for (ExceptionHelp ehelp : app.getExceptionHelps()) {
        String hint = ehelp.getI18nHint();
        if (hint.isEmpty()) {
          errors++;
          if (showError) {
            System.err.println(
                "ExceptionHelp hint for " + ehelp.getException() + " is empty");
          }
        } else {
          errors += checkText(hint);
        }
      }
    }
    assertEquals(0, errors);
  }

  @Test
  public void testPropertiesAreFields() throws Exception {
    boolean show = true;
    List<String> fieldList = new ArrayList<String>();
    for (Field field : this.getI18nClass().getFields()) {
      fieldList.add(field.getName());
    }
    int errors = 0;
    for (String locale : locales) {
      ResourceBundle bundle = Translator.initialize(getI18nName(), locale);
      Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        String constantName = asUnderScore(key);
        if (!fieldList.contains(constantName)) {
          errors++;
          if (show)
            System.out.println("  public static final String " + constantName
                + "=\"" + key + "\"; //" + bundle.getString(key));
        }
      }
    }
    assertEquals(0, errors);
  }

}

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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bitplan.gui.App;
import com.bitplan.gui.Control;
import com.bitplan.gui.ExceptionHelp;
import com.bitplan.gui.Form;
import com.bitplan.gui.Group;
import com.bitplan.gui.Menu;
import com.bitplan.gui.MenuItem;

/**
 * check the translation for an Application - each Application might want to
 * implement an extension for this test
 * 
 * @author wf
 *
 */
public abstract class TestI18n {
  @BeforeClass
  public static void unsetLenient() {
    Translator.lenient = false;
  }

  @SuppressWarnings("rawtypes")
  public abstract Class getI18nClass();

  public abstract App getApp() throws Exception;

  public abstract String getI18nName();

  protected static boolean show = false;
  protected static boolean showError = true;
  public static boolean withFieldCheck = true;
  String locales[] = { "en", "de" };

  /**
   * collect the I18n Fields
   * 
   * @author wf
   *
   */
  public class I18nFields {
    String locale;
    List<String> fieldList = new ArrayList<String>();
    Map<String, String> constantForKey = new HashMap<String, String>();

    /**
     * construct me for the given locale
     * 
     * @param locale
     */
    public I18nFields(String locale) {
      this.locale = locale;
      for (Field field : getI18nClass().getFields()) {
        fieldList.add(field.getName());
      }
      ResourceBundle bundle = Translator.initialize(getI18nName(), locale);
      Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        String constantName = asUnderScore(key);
        constantForKey.put(key, constantName);
      }
    }
  }

  /**
   * Helper to collect Check results
   * 
   * @author wf
   *
   */
  public class Check {
    Control control;
    public String translated;
    public Throwable th;
    @SuppressWarnings("rawtypes")
    private Class clazz;
    private String text;
    private String i18nId;
    private String hint;

    /**
     * construct me
     * 
     * @param clazz
     * @param text
     * @param i18nId
     * @param hint
     */
    @SuppressWarnings("rawtypes")
    public Check(Class clazz, String text, String i18nId, String hint) {
      this.clazz = clazz;
      this.text = text;
      this.i18nId = i18nId;
      this.hint = hint;
    }

    /**
     * return me as a property for the I18n resource bundle
     * 
     * @return - the string
     */
    public String asProperty() {
      String property = String.format("#%s %s\n%s=%s", clazz.getName(), hint,
          i18nId, th == null ? translated : text);
      return property;
    }
  }

  public class CheckList {
    int errors = 0;
    String locale;
    List<Check> checks = new ArrayList<Check>();
    private I18nFields i18nFields;

    public CheckList(String locale) {
      this.locale = locale;
      i18nFields = new I18nFields(locale);
    }

    /**
     * set the errors
     * 
     * @param checkMenu
     */
    public void setErrors() {
      for (Check check : checks) {
        if (check.th != null)
          errors++;
      }
    }

    /**
     * show the Checks
     */
    public void showChecks() {
      if ((show || showError) && errors > 0) {
        System.out.println("# locale " + locale);
        for (Check check : checks) {
          if (check.th != null) {
            System.out.println(check.asProperty());
          }
        }
      }
    }

    /**
     * show the result for the constant checks
     */
    public void showConstantChecks() {
      if ((show || showError) && errors > 0) {
        System.out.println("# locale " + locale);
        for (Check check : checks) {
          String constantName = this.i18nFields.constantForKey
              .get(check.i18nId);
          if (constantName == null) {
            System.out.println(String.format("#%s %s missing\n%s=", check.hint,check.clazz.getName(),check.i18nId));
          } else {
            if (!this.i18nFields.fieldList.contains(constantName)) {
              System.out.println("  public static final String " + constantName
                  + "=\"" + check.i18nId + "\"; //" + check.translated);
            }
          }
        }
      }
    }

    /**
     * set errors and show checks
     * 
     * @return - the number of errors found
     */
    public int setErrorsAndShowChecks() {
      this.setErrors();
      this.showChecks();
      this.showConstantChecks();
      return this.errors;
    }
  }

  /**
   * get the underscored version of the given identifier
   * 
   * @param identifier
   * @return - the underscored identifier
   */
  public static String asUnderScore(String identifier) {
    // first Replace dots with underscores
    identifier = identifier.replaceAll("\\.", "__");
    // then replace CamelCase with Underscores and change to upper case
    String underScoredIdentifier = identifier
        .replaceAll("(.)(\\p{Upper})", "$1_$2").toUpperCase();
    // remove triple underscores
    underScoredIdentifier = underScoredIdentifier.replaceAll("___", "__");
    return underScoredIdentifier;
  }

  /**
   * check the given text whether it is available as a translate
   * 
   * @param control
   * @param i18nId
   * @param hint
   * @return 0 if translated 1 if not
   */
  public Check checkText(Control control, String i18nId, String hint) {
    return this.checkText(control.getClass(), control.getTitle(), i18nId, hint);
  }

  /**
   * check the given text for an object of the given class
   * 
   * @param clazz
   * @param text
   *          - default translation
   * @param i18nId
   * @param hint
   * @return - the error
   */
  @SuppressWarnings("rawtypes")
  public Check checkText(Class clazz, String text, String i18nId, String hint) {
    Check check = new Check(clazz, text, i18nId, hint);
    try {
      check.translated = I18n.get(i18nId);
    } catch (Throwable th) {
      check.th = th;
    }
    return check;
  }

  /**
   * check the translation of the given menu
   * 
   * @param menu
   *          - the menu to check
   * @return - the number of errors
   */
  public List<Check> checkMenu(Menu menu) {
    List<Check> checks = new ArrayList<Check>();
    List<Menu> submenus = menu.getSubMenus();
    if (submenus.size() == 0)
      checks.add(checkText(menu, menu.getI18nId(),
          menu.getTitle() + "/" + menu.getShortCut()));
    for (Menu submenu : submenus) {
      checks.addAll(checkMenu(submenu));
    }
    for (MenuItem menuItem : menu.getMenuItems()) {
      checks.add(checkText(menuItem, menuItem.getI18nId(),
          menuItem.getTitle() + "/" + menuItem.getShortCut()));
    }
    return checks;
  }

  @Test
  public void testMenuTranslated() throws Exception {
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      CheckList checkList = new CheckList(locale);
      Translator.initialize(getI18nName(), locale);
      checkList.checks = checkMenu(app.getMainMenu());
      errors += checkList.setErrorsAndShowChecks();
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
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      CheckList checkList = new CheckList(locale);
      Translator.initialize(getI18nName(), locale);
      for (Field field : getI18nClass().getFields()) {
        String text = (String) field.get(null);
        checkList.checks.add(checkText(field.getClass(), field.getName(), text,
            field.getName()));
      }
      checkList.checks.addAll(checkMenu(app.getMainMenu()));
      errors += checkList.setErrorsAndShowChecks();
    }
    assertEquals(0, errors);
  }

  @Test
  public void testForms() throws Exception {
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      CheckList checkList = new CheckList(locale);
      Translator.initialize(getI18nName(), locale);
      for (Group group : app.getGroups()) {
        checkList.checks.add(checkText(group, group.getI18nId(),
            group.getTitle() + "/" + group.getIcon()));
        for (Form form : group.getForms()) {
          checkList.checks.add(checkText(form, form.getI18nId(),
              form.getTitle() + "/" + form.getIcon()));
          if (withFieldCheck)
            for (com.bitplan.gui.Field field : form.getFields()) {
              checkList.checks
                  .add(checkText(field, field.getI18nId(), field.getTitle()));
            }
        }
      }
      errors += checkList.setErrorsAndShowChecks();
    }
    assertEquals(0, errors);
  }

  @Test
  public void textExceptionHelp() throws Exception {
    App app = getApp();
    int errors = 0;
    for (String locale : locales) {
      CheckList checkList = new CheckList(locale);
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
          checkList.checks.add(checkText(ehelp.getClass(), ehelp.getI18nHint(),
              hint, ehelp.getUrl()));
        }
      }
      errors += checkList.setErrorsAndShowChecks();
    }
    assertEquals(0, errors);
  }

}

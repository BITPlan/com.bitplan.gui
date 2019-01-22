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
package com.bitplan.appconfig;

import java.io.File;
import java.util.Map;

import com.bitplan.error.ErrorHandler;
import com.bitplan.i18n.Translator;
import com.bitplan.json.JsonAble;
import com.bitplan.json.JsonManager;
import com.bitplan.json.JsonManagerImpl;

/**
 * application Preferences
 * 
 * @author wf
 *
 */
public class Preferences implements JsonAble {
  public enum LangChoice {
    en, de, notSet
  }

  LangChoice language = LangChoice.notSet;
  Boolean debug = false;
  Boolean autoStart = false;
  Integer screenPercent = 100;
  Integer darkBrightness = 32;
  Integer lightBrightness = 255;

  String logDirectory = Translator.APPLICATION_PREFIX + "Logs";
  String screenShotDirectory = Translator.APPLICATION_PREFIX + "ScreenShots";
  String logPrefix = Translator.APPLICATION_PREFIX; // e.g. my Ion

  public LangChoice getLanguage() {
    return language;
  }

  public void setLanguage(LangChoice language) {
    this.language = language;
  }

  public Boolean getAutoStart() {
    return autoStart;
  }

  public void setAutoStart(Boolean autoStart) {
    this.autoStart = autoStart;
  }

  public Boolean getDebug() {
    return debug;
  }

  public void setDebug(Boolean debug) {
    this.debug = debug;
  }

  public int getScreenPercent() {
    return screenPercent;
  }

  public void setScreenPercent(int screenPercent) {
    this.screenPercent = screenPercent;
  }

  public int getDarkBrightness() {
    return darkBrightness;
  }

  public void setDarkBrightness(int darkBrightness) {
    this.darkBrightness = darkBrightness;
  }

  public int getLightBrightness() {
    return lightBrightness;
  }

  public void setLightBrightness(int lightBrightness) {
    this.lightBrightness = lightBrightness;
  }

  public String getLogPrefix() {
    return logPrefix;
  }

  public void setLogPrefix(String logPrefix) {
    this.logPrefix = logPrefix;
  }

  public String getLogDirectory() {
    return logDirectory;
  }

  public void setLogDirectory(String logDirectory) {
    this.logDirectory = logDirectory;
  }

  public String getScreenShotDirectory() {
    return screenShotDirectory;
  }

  public void setScreenShotDirectory(String screenShotDirectory) {
    this.screenShotDirectory = screenShotDirectory;
  }

  /**
   * get the number for the given eky
   * @param map
   * @param key
   * @return the number
   */
  public Integer getNumber(Map<String, Object> map, String key) {
    Object value=map.get(key);
    if (value != null) {
      if (value instanceof Double)
        return ((Double) value).intValue();
      else
        return (Integer) value;
    }
    return null;
  }
  @Override
  public void fromMap(Map<String, Object> map) {
    Object langChoice = map.get("language");
    if (langChoice instanceof LangChoice)
      this.setLanguage((LangChoice) langChoice);
    else {
      String langChoiceStr = (String) langChoice;
      if (langChoiceStr != null)
        this.setLanguage(LangChoice.valueOf(langChoiceStr));
    }
    this.setDebug((Boolean) map.get("debug"));
    this.autoStart = (Boolean) map.get("autoStart");
    this.screenPercent = getNumber(map,"screenPercent");
    this.lightBrightness=getNumber(map,"lightBrightness");
    this.darkBrightness=getNumber(map,"darkBrightness");
    this.logPrefix = (String) map.get("logPrefix");
    this.logDirectory = (String) map.get("logDirectory");
    this.screenShotDirectory = (String) map.get("screenShotDirectory");
  }

  @Override
  public void reinit() {

  }

  static Preferences instance;

  /**
   * get an instance of the preferences
   * 
   * @return - the instance
   * @throws Exception
   */
  public static Preferences getInstance() {
    if (instance == null) {
      File jsonFile = JsonAble.getJsonFile(Preferences.class.getSimpleName());
      if (jsonFile.canRead()) {
        JsonManager<Preferences> jmPreferences = new JsonManagerImpl<Preferences>(
            Preferences.class);
        try {
          instance = jmPreferences.fromJsonFile(jsonFile);
        } catch (Exception e) {
          ErrorHandler.handle(e);
        }
      }
      if (instance == null)
        instance = new Preferences();
    }
    return instance;
  }
}

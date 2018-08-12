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
package com.bitplan.json;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.bitplan.i18n.Translator;

/**
 * for jsonable POJOs
 * 
 * @author wf
 *
 */
public interface JsonAble extends AsJson, JsonValueMap {
  public static boolean debug = true;

  /**
   * reinitialize me after being reloaded from json
   */
  public default void reinit() {
    // override as you like
  };

  /**
   * save me to my Json File
   * 
   * @throws IOException
   */
  default void save() throws IOException {
    File jsonFile = getJsonFile();
    if (!jsonFile.getParentFile().isDirectory())
      jsonFile.getParentFile().mkdirs();
    FileUtils.writeStringToFile(jsonFile, this.asJson(), "UTF-8");
  }

  default File getJsonFile() {
    File jsonFile = getJsonFile(this.getClass().getSimpleName());
    return jsonFile;
  }

  /**
   * get the json file for the given appName
   * 
   * @param appName
   * @return the Json File
   */
  static File getJsonFile(String appName) {
    String home = System.getProperty("user.home");
    File configDirectory = new File(
        home + "/." + Translator.APPLICATION_PREFIX + "/");
    String jsonFileName = appName + ".json";
    File jsonFile = new File(configDirectory, jsonFileName);
    return jsonFile;
  }

  /**
   * get my values a a map
   * 
   * @return - the map of values
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  default Map<String, Object> asMap() {
    Map<String, Object> map = new HashMap<String, Object>();
    String json = this.asJson();
    map = (Map<String, Object>) getGson().fromJson(json, map.getClass());
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      String typename = field.getType().getName();
      // System.out.println(typename);
      switch (typename) {
      case "int":
      case "java.lang.Integer":
        Double di = (Double) map.get(field.getName());
        if (di != null)
          map.put(field.getName(), di.intValue());
        break;
      case "java.lang.Long":
      case "long":
        Double dl = (Double) map.get(field.getName());
        if (dl != null)
          map.put(field.getName(), dl.longValue());
        break;
      case "java.util.Date":
        String dtext = (String) map.get(field.getName());
        if (dtext != null) {
          Date date;
          try {
            date = JsonManagerImpl.dateFormat.parse(dtext);
            map.put(field.getName(), date);
          } catch (ParseException e) {
            if (debug)
              e.printStackTrace();
          }
        }
        break;
      default:
        if (field.getType().isEnum()) {
          String value = (String) map.get(field.getName());
          if (value != null)
            map.put(field.getName(),
                Enum.valueOf((Class<Enum>) field.getType(), value));
        }
      }
    }

    /*
     * Jackson version
     * // https://stackoverflow.com/a/39544594/1497139
     * ObjectMapper mapper = new ObjectMapper();
     * mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
     * // Convert POJO to Map
     * Map<String, Object> map = mapper.convertValue(this,
     * new TypeReference<Map<String, Object>>() {
     * });
     */
    return map;
  }

  /**
   * initialize my values from the given map
   * 
   * @param map
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public default void fromMap(Map<String, Object> map) {
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      String fieldName = field.getName();
      field.setAccessible(true);
      if (map.containsKey(fieldName)) {
        try {
          Object value=map.get(fieldName);
          if (value instanceof String && field.getType().isEnum()) {
            value=Enum.valueOf((Class<Enum>) field.getType(), (String)value);
          }
          field.set(this, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          if (debug)
            e.printStackTrace();
        }
      }
    }
  }

}

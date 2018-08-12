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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.bitplan.error.ErrorHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * implementation of JSonManager
 * 
 * @author wf
 *
 */
public class JsonManagerImpl<T extends JsonAble> implements JsonManager<T> {
  public static transient final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  public static transient final DateFormat dateFormat=new SimpleDateFormat(DATE_FORMAT);
  Class<T> clazz;
  T instance;

  /**
   * get the Instance
   */
  public T getInstance() {
    File jsonFile = JsonAble.getJsonFile(clazz.getSimpleName());
    try {
      if (jsonFile.canRead()) {
        instance = fromJsonFile(jsonFile);
      }
      if (instance == null) {
        Constructor<T> ctor;
        ctor = clazz.getConstructor();
        instance = ctor.newInstance();
      }
    } catch (Throwable th) {
      ErrorHandler.handle(th);
    }
    return instance;
  }

  /**
   * construct me for the given class
   * 
   * @param clazz
   */
  public JsonManagerImpl(Class<T> clazz) {
    super();
    this.clazz = clazz;
  }

  /**
   * get a T from the given Json File
   * 
   * @param jsonFile
   *          - the json File to get the T from
   * @return - the T
   * @throws Exception
   *           of reading from json fails
   */
  public T fromJsonFile(File jsonFile) throws Exception {
    if (jsonFile.canRead())
      return fromJsonStream(new FileInputStream(jsonFile));
    else
      return null;
  }

  /**
   * get the T from the given Json Stream
   * 
   * @param jsonStream
   * @return the Json Stream
   * @throws Exception
   */
  public T fromJsonStream(InputStream jsonStream) throws Exception {
    Gson gson = this.getGson();
    T newT = gson.fromJson(new InputStreamReader(jsonStream), clazz);
    newT.reinit();
    return newT;
  }

  @Override
  public T fromJson(String json) {
    Gson gson = this.getGson();
    T newT = gson.fromJson(json, clazz);
    newT.reinit();
    return newT;
  }

  /**
   * get the Gson
   * 
   * @return the Gson implementation
   */
  public static Gson getGsonStatic() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    // new GraphAdapterBuilder().addType(Pid.class).registerOn(gsonBuilder);
    // https://stackoverflow.com/a/26631632/1497139
    Gson gson = gsonBuilder.setPrettyPrinting().setDateFormat(DATE_FORMAT).create();
    return gson;
  }

  @Override
  public Gson getGson() {
    return getGsonStatic();
  }

}

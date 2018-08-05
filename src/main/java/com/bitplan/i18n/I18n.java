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

/**
 * Internationalization interface
 * @author wf
 *
 */
public interface I18n {
  
  /**
   * Translate the given text
   * 
   * @param text
   *          - the text to translate
   * @return - the text
   */
  public static String get(String text) {
    return Translator.translate(text);
  }

  /**
   * translate the given text with the given params
   * 
   * @param text
   * @param params
   * @return the translated string
   */
  public static String get(String text, Object... params) {
    return Translator.translate(text, params);
  }
}

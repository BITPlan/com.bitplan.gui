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

import java.util.Map;

public interface JsonValueMap extends ValueMap {

  public Map<String, Object> asMap();

  /**
   * initialize my values from the given map
   * 
   * @param map
   */
  public void fromMap(Map<String, Object> map);

  /**
   * set my values from the given map
   * 
   * @param valueMap
   */
  public default void setValues(Map<String, Object> valueMap) {
    fromMap(valueMap);
  }

  /**
   * get the values
   * 
   * @return the valueMap
   */
  public default Map<String, Object> getValueMap() {
    return asMap();
  };
}

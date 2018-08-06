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

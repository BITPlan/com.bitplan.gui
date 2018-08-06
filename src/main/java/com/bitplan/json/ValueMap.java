package com.bitplan.json;

import java.util.Map;

public interface ValueMap {

  /**
   * set my values from the given map
   * 
   * @param valueMap
   */
  public void setValues(Map<String, Object> valueMap);

  /**
   * get the values
   * 
   * @return the valueMap
   */
  public Map<String, Object> getValueMap();
}

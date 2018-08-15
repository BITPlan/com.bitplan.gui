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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.bitplan.json.TestJson.PoJo.BaseColor;

/**
 * test the json Handling
 * @author wf
 *
 */
public class TestJson {
  public static boolean debug=true;
  public class Person implements JsonAble {
    String name;
    String firstname;
    String eMail;
    @Override
    public void reinit() {
      
    }
    @Override
    public void fromMap(Map<String, Object> map) {
      name=(String)map.get("name");
      firstname=(String)map.get("firstname");
      eMail=(String)map.get(eMail);
    }
  }
  
  @Test
  public void testPerson() {
    Person person=new Person();
    person.name="Doe";
    person.firstname="John";
    person.eMail="john@doe.org";
    String json=person.asJson();
    assertEquals("{\n" + 
        "  \"name\": \"Doe\",\n" + 
        "  \"firstname\": \"John\",\n" + 
        "  \"eMail\": \"john@doe.org\"\n" + 
        "}",json);
  }
  
  public static class PoJo implements JsonAble {
    enum BaseColor {Red,Green,Blue};
    BaseColor baseColor;
    Date date;
    int i;
    Integer i2;
    long l;
    Long l2;
    double d;
    Double d2;
    boolean b;
    Boolean b1;
    String s="txt";
    Object o=null;
    
    public PoJo() {};
    public PoJo(BaseColor baseColor,Date date,int i, Integer i2, long l, Long l2, double d, Double d2,
        boolean b, Boolean b1, String s, Object o) {
      super();
      this.baseColor=baseColor;
      this.date=date;
      this.i = i;
      this.i2 = i2;
      this.l = l;
      this.l2 = l2;
      this.d = d;
      this.d2 = d2;
      this.b = b;
      this.b1 = b1;
      this.s = s;
      this.o = o;
    }
    
  }
  
  @Test
  public void testMap() {
    Date now=new Date(1534077204741L);
    PoJo pojo=new PoJo(BaseColor.Red,now,1,2,3,4L,5.0,6.0,false,true,"txt",null);
    String json="{\n" + 
        "  \"baseColor\": \"Red\",\n" + 
        "  \"date\": \"2018-08-12T14:33:24.741\",\n" + 
        "  \"i\": 1,\n" + 
        "  \"i2\": 2,\n" + 
        "  \"l\": 3,\n" + 
        "  \"l2\": 4,\n" + 
        "  \"d\": 5.0,\n" + 
        "  \"d2\": 6.0,\n" + 
        "  \"b\": false,\n" + 
        "  \"b1\": true,\n" + 
        "  \"s\": \"txt\"\n" + 
        "}";
    if (debug)
      System.out.println(pojo.asJson());
    assertEquals(json,pojo.asJson());
    Map<String, Object> map = pojo.asMap();
    assertEquals(11,map.size());
    if (debug)
    for (Entry<String, Object> mapEntry:map.entrySet()) {
      System.out.println(String.format("%s (%s)=%s",mapEntry.getKey(),mapEntry.getValue().getClass().getName(),""+mapEntry.getValue()));
    }
    assertTrue(map.get("l") instanceof Long);
    assertTrue(map.get("l2") instanceof Long);
    assertTrue(map.get("i") instanceof Integer);
    assertTrue(map.get("i2") instanceof Integer);
    PoJo pojo2=new PoJo();
    pojo2.fromMap(map);
    assertEquals(json,pojo2.asJson());
  }
  
  @Test
  public void testTimeZoneHandling() {
    PoJo pojo=new PoJo();
    Date now=new Date(1534077204741L);
    pojo.date=now;
    DateFormat isoTimeZone=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    String json=pojo.asJson();
    String nowStr = JsonManagerImpl.dateFormat.format(now);
    String isoTimeZoneStr=isoTimeZone.format(now);
    if (debug) {
      System.out.println(json);
      System.out.println(nowStr);
      System.out.println(isoTimeZoneStr);
    }
    assertTrue(pojo.asJson().contains(nowStr));
    assertTrue(isoTimeZoneStr.startsWith(nowStr));
  }
}

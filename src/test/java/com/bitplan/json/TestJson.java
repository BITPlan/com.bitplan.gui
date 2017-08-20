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

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * test the json Handling
 * @author wf
 *
 */
public class TestJson {

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
}

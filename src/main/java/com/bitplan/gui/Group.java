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
package com.bitplan.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a group of forms
 * @author wf
 *
 */
public class Group implements Control {
  String id;
  String title;
  String icon;
  private List<Form> forms = new ArrayList<Form>();
  private Map<String,Form> formById=new HashMap<String,Form>();
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public List<Form> getForms() {
    return forms;
  }

  public void setForms(List<Form> forms) {
    this.forms = forms;
  }
  /**
   * get the form by the given id
   * @param id
   * @return the form by id
   */
  public Form getFormById(String id) {
    Form form=this.formById.get(id);
    return form;
  }
  
  /**
   * reinitialization of data structures
   */
  public void reinit() {
    if (this.id==null)
      if (this.title!=null)
        this.id=title+"Group";
    for (Form form:this.getForms()) {
      form.reinit();
      if (form.getId()!=null)
        this.formById.put(form.getId(), form);
    }
  }

  @Override
  public String getI18nId() {
    return this.id;
  }
}

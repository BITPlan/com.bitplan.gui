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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * test the GUI handling
 * @author wf
 *
 */
public class TestGUI {

  @Test
  public void testGuiDescription() throws Exception{
    App app=new App();
    app.name="EVChargeTrack";
    Menu mainMenu=new Menu();
    mainMenu.id="mainMenu";
    String []items={"File","Edit","Window","Help"};
    for (String item:items) {
      MenuItem menuItem=new MenuItem();
      menuItem.setId(item);
      menuItem.setTitle(item);
      menuItem.setShortCut(item.substring(1,2));
      mainMenu.getMenuItems().add(menuItem);
      assertEquals(item,menuItem.getId());
      assertEquals(item,menuItem.getTitle());
      assertEquals(1,menuItem.getShortCut().length());
    }
    app.setMainMenu(mainMenu);
    String json=app.asJson();
    // System.out.println(json);
    File path=File.createTempFile("TestGUI", ".json");
    FileUtils.write(path, json, "UTF-8");
    App app2 = App.getInstance(path);
    assertNotNull(app2);
    assertNotNull(app2.getMainMenu());
    assertEquals(items.length,app2.getMainMenu().menuItems.size());
    assertEquals(app2.asJson(),app.asJson());
  }
  
  @Test
  /**
   * test the automatic assignment of ids and i18nids from the title attributes if no id has been actively set
   */
  public void testI18nId() {
    Menu menu=new Menu();
    menu.setTitle("menu");
    assertEquals("menu",menu.getTitle());
    MenuItem menuItem=new MenuItem();
    menuItem.setTitle("menuItem");
    menu.menuItems.add(menuItem);
    assertEquals(1,menu.getMenuItems().size());
    menu.reinit();
    assertEquals("menuMenu.menuItemMenuItem",menuItem.getId());
    assertEquals(menuItem.getId(),menuItem.getI18nId());
  }
}

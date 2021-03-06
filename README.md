### com.bitplan.gui
[generic GUI interface description](http://www.bitplan.com/Com.bitplan.gui)

[![Travis (.org)](https://img.shields.io/travis/BITPlan/com.bitplan.gui.svg)](https://travis-ci.org/BITPlan/com.bitplan.gui)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitplan.gui/com.bitplan.gui.svg)](https://search.maven.org/artifact/com.bitplan.gui/com.bitplan.gui/0.0.15/jar)
[![codecov](https://codecov.io/gh/BITPlan/com.bitplan.gui/branch/master/graph/badge.svg)](https://codecov.io/gh/BITPlan/com.bitplan.gui)
[![GitHub issues](https://img.shields.io/github/issues/BITPlan/com.bitplan.gui.svg)](https://github.com/BITPlan/com.bitplan.gui/issues)
[![GitHub issues](https://img.shields.io/github/issues-closed/BITPlan/com.bitplan.gui.svg)](https://github.com/BITPlan/com.bitplan.gui/issues/?q=is%3Aissue+is%3Aclosed)
[![GitHub](https://img.shields.io/github/license/BITPlan/com.bitplan.gui.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![BITPlan](http://wiki.bitplan.com/images/wiki/thumb/3/38/BITPlanLogoFontLessTransparent.png/198px-BITPlanLogoFontLessTransparent.png)](http://www.bitplan.com)

### Documentation
* [Wiki](http://www.bitplan.com/Com.bitplan.gui)
* [com.bitplan.gui Project pages](https://BITPlan.github.io/com.bitplan.gui)
* [Javadoc](https://BITPlan.github.io/com.bitplan.gui/apidocs/index.html)
* [Test-Report](https://BITPlan.github.io/com.bitplan.gui/surefire-report.html)
### Maven dependency

Maven dependency
```xml
<!-- generic GUI interface description http://www.bitplan.com/Com.bitplan.gui -->
<dependency>
  <groupId>com.bitplan.gui</groupId>
  <artifactId>com.bitplan.gui</artifactId>
  <version>0.0.15</version>
</dependency>
```

[Current release at repo1.maven.org](http://repo1.maven.org/maven2/com/bitplan/gui/com.bitplan.gui/0.0.15/)

### How to build
```
git clone https://github.com/BITPlan/com.bitplan.gui
cd com.bitplan.gui
mvn install
```
### Version History
* 2017-08-20 0.0.1 - initial release
* 2017-08-21 0.0.2 - adds icon to groups
* 2017-08-23 0.0.3 - adds Presenter interface
* 2018-08-05 0.0.4 - improves I18n support adds link to documentation
* 2018-08-05 0.0.5 - fixes incomplete release to maven central
* 2018-08-06 0.0.6 - improves I18n support and TestI18n utility
* 2018-08-06 0.0.7 - refactors to provide getI18nId
* 2018-08-07 0.0.8 - refactors to provide getInstance
* 2018-08-09 0.0.9 - refactors to add StopWatch interface
* 2018-08-10 0.0.10 - fixes getInstance
* 2018-08-11 0.0.11 - improves JsonAble
* 2018-08-17 0.0.12 - removes debug option from JsonAble since its final
* 2019-01-09 0.0.13 - fixes #1 LangChoice in Preferences fromMap
* 2019-01-09 0.0.14 - 0.0.13 rerelease to fix pom config issues
* 2019-01-22 0.0.15 - adds brightness values to Preferences

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AARLinkSources-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1288)

## This plugin in deprecated. AndroidStudio 1.4 has this support built in.

AARLinkSources Plugin
====
The AARLinkSources Plugin is designed to attach sources for .aar dependencies in AndroidStudio.

Setup
----
~~~groovy
buildscript {
    repositories {
        maven { url 'https://raw.github.com/xujiaao/mvn-repository/master/releases' }
    }

    dependencies {
        classpath 'com.github.xujiaao:aarLinkSources:1.0.0'
    }
}
~~~

Usage
----
~~~groovy
apply plugin: 'aar-link-sources'
~~~




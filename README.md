AARLinkSources Plugin
====
The AARLinkSources Plugin is designed to attach sources for .aar dependencies in AndroidStudio.

Setup
----
~~~
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
~~~
apply plugin: 'aar-link-sources'

dependencies {
    compile 'com.android.support:support-v4:20.0.0'
    aarLinkSources 'com.android.support:support-v4:20.0.0:**sources@jar**'
}
~~~






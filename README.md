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

dependencies {
    compile 'com.android.support:support-v4:21.0.3'
    aarLinkSources 'com.android.support:support-v4:21.0.3:sources@jar'
}
~~~

Tips
----
If you want to see the debug log, just add this line in your build script.
~~~groovy
rootProject.aarLinkSources.debug = true
~~~

Output:
~~~
[AARLinkSources] [Info] Link sources: support-v4-21.0.3-sources.jar
[AARLinkSources] [Info] Link success: support_v4_21_0_3.xml
~~~






# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zenofoltin/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
-dontwarn okhttp3.**

-keepattributes InnerClasses, Signature, *Annotation*, Exceptions, EnclosingMethod
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keep class io.reactivex.** { *; }
-keep class org.reactivestreams.** { *; }

-keep class com.zedeff.marvellous.** { *; }
-keepclassmembers enum com.zedeff.marvellous.** { *; }

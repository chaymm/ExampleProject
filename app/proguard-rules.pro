# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\android-sdk/tools/proguard/proguard-android.txt
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

#设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
#混淆时不会产生形形色色的类名
-dontusemixedcaseclassnames
#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不缩减代码
-dontshrink
#不预校验
-dontpreverify
#混淆后生产映射文件 map 类名->转化后类名的映射
-verbose
#-dontoptimize
#混淆采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#不优化泛型和反射
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

#第三方jar包都申明进来
-libraryjars libs/afinal_0.5.1_bin.jar
-libraryjars libs/android-async-http-1.4.5.jar
-libraryjars libs/jackson-all-1.9.10.jar
-libraryjars libs/httpmime-4.1.1.jar
-libraryjars libs/jzlib-1.1.3.jar

#不对第三方的jar包的提出WARN
-dontwarn android.support.v4.**
-dontwarn org.apache.commons.net.**
-dontwarn com.google.**
-dontwarn com.jcraft.**
-dontwarn com.loopj.android.**
-dontwarn org.codehaus.**
-dontwarn org.apache.http.entity.**

#将你不需要混淆的部分申明进来，因为有些类经过混淆会导致程序编译不通过
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService


#对第三方jar包的类不进行混淆
-keep class android.support.v4.** { *; }
-keep class com.google.** { *; }
-keep class com.jcraft.** { *; }
-keep class com.loopj.android.** { *; }
-keep class org.codehaus.** { *; }
-keep class org.apache.http.entity.** { *; }
-keep class com.netease.nis.bugrpt.**{ *; }

#natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#对于所有类，有这个构造函数不进行混淆,主要是为了在layout中的，自定义的view
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepnames class * implements android.view.View.OnClickListener
-keepclassmembers class * implements android.view.View.OnClickListener {
    public void onClick(android.view.View);
}

-keepnames class * implements android.widget.Chronometer.OnChronometerTickListener
-keepclassmembers class * implements android.widget.Chronometer.OnChronometerTickListener {
    public void onChronometerTick(android.widget.Chronometer);
}

-keepnames class * implements android.view.SurfaceHolder.Callback
-keepclassmembers class * implements android.view.SurfaceHolder.Callback {
    public void surfaceCreated(android.view.SurfaceHolder);
    public void surfaceDestroyed(android.view.SurfaceHolder);
    public void surfaceChanged(android.view.SurfaceHolder, java.lang.Integer, java.lang.Integer,java.lang.Integer);
}

-keepnames class * implements android.media.MediaPlayer.OnCompletionListener
-keepclassmembers class * implements android.media.MediaPlayer.OnCompletionListener {
    public void onCompletion(android.media.MediaPlayer);
}
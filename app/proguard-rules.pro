#---------------------------------定制化区域---------------------------
#--------------------------1.实体类-----------------------------------


#--------------------------2.第三方包----------------------------------
#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#muppdf
-dontwarn com.artifex.**
-keep class com.artifex.mupdfdemo.** { *; }

#floatingGroupExpandableListView
-dontwarn com.diegocarloslima.fgelv.**
-keep class com.diegocarloslima.fgelv.** { *; }

#gson,zxing
-dontwarn com.google.**
-keep class com.google.** { *; }

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#rxjava rxAndroid1.0
#-dontwarn sun.misc.**
#-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
# long producerIndex;
# long consumerIndex;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode producerNode;
#}
#-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
# rx.internal.util.atomic.LinkedQueueNode consumerNode;
#}

#rxlifecycle2
-dontwarn javax.annotation.**

#--------------------------3.与js互相调用的类----------------------------


#--------------------------4.反射相关的类和方法---------------------------


#--------------------------5.其他类-----------------------------------




#--------------------------------基本不用动区域--------------------------
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

#将你不需要混淆的部分申明进来，因为有些类经过混淆会导致程序编译不通过
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class * extends android.support.**
-keep class android.support.** { *; }

-dontwarn android.support.**

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

-keep class **.R$*{
	*;
}

-keepclassmembers class *{
	void *(**On*Event);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient{
	public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
	public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient{
	public void *(android.webkit.WebView,java.lang.String);
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



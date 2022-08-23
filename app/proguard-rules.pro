# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lirui/Library/Android/sdk/tools/proguard/proguard-android.txt
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


#------------------------------项目定制化区域 start----------------------------------------------

#---------------------------------1.实体类--------------------------------------
#涉及到与服务端的交互，各种gson的交互如此等等，是要保留的
#修改成项目实体类对应的包名 如果实体类不在一个包下，需要添加多个
-keep class com.live.fox.entity.** { *; }
#----------------------------------------------------------------------------


#---------------------------------2.第三方包-------------------------------
-keep class com.ut.** {*;}
-dontwarn com.ut.**

-keep class com.ta.** {*;}
-dontwarn com.ta.**

-keep class anet.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**

-keep class org.apache.http.**
-keep interface org.apache.http.**
-dontwarn org.apache.**

# OKhttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}

#sharesdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}

-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R**

# zxing
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**

# okhttp
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.*{*;}


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}


# Eventbus 3.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


# Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe* { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}


# Support-v4
#https://stackoverflow.com/questions/18978706/obfuscate-android-support-v7-widget-gridlayout-issue
-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }


# Support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

# Support Design
#@link http://stackoverflow.com/a/31028536
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


# Rxjava RxAndroid
-dontwarn rx.*
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef* {
    rx.internal.util.atomic.LinkedQueueNode* producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef* {
    rx.internal.util.atomic.LinkedQueueNode* consumerNode;
}


# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrolambda
-dontwarn java.lang.invoke.*

# Fresco
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Tencent推拉流
-keep class com.tencent.** { *; }
-keep class com.tencent.imsdk.** { *; }

# SVGAPlayer
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

-dontwarn javax.annotation.**
-dontwarn java.lang.**
-dontwarn org.conscrypt.*
-dontwarn androidx.annotation.*
-dontwarn com.facebook.infer.annotation.*

# 主播端混淆 faceunity 遇到这些问题需要排除
-dontwarn javax.naming.**
-dontwarn android.bluetooth.**
-dontwarn android.os.ServiceManager

# Activityrouter混淆配置
-keep class com.github.mzule.activityrouter.router.** { *; }

# RealtimeBlurView
-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

# FaceUnity
-keep class com.faceunity.wrapper.faceunity {*;}

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

# 阿里OSS
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

#-------------------------------------------------------------------------


#---------------------------------3.与js互相调用的类------------------------
#在这下面写与js互相调用的类，没有就去掉这句话！
#-keep class 你的类所在的包.** { *; }
#如果是内部类的话，你可以这样
#-keepclasseswithmembers class 你的类所在的包.父类$子类 { <methods>; }
#例如:-keepclasseswithmembers class com.demo.login.bean.ui.MainActivity$JSInterface {
#          <methods>;
#    }

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------
#在这下面写反射相关的类和方法，没有就不用写！


#-------------------------------------------------------------------------

#------------------------------项目定制化区域 end----------------------------------------------


#------------------------------基本不用动区域 start--------------------------------------------
####################################################################################################
#一般以下情况都会不混淆：
#1.使用了自定义控件那么要保证它们不参与混淆
#2.使用了枚举要保证枚举不被混淆
#3.对第三方库中的类不进行混淆
#4.运用了反射的类也不进行混淆
#5.使用了 Gson 之类的工具要使 JavaBean 类即实体类不被混淆
#6.在引用第三方库的时候，一般会标明库的混淆规则的，建议在使用的时候就把混淆规则添加上去，免得到最后才去找
#7.有用到 WebView 的 JS 调用也需要保证写的接口方法不混淆，原因和第一条一样
#8.Parcelable 的子类和 Creator 静态成员变量不混淆，否则会产生 Android.os.BadParcelableException 异常
#9.Android四大组件和Application最好也不要混淆

#---------------------------------基本指令区----------------------------------
-optimizationpasses 5                      # 指定代码的压缩级别  在0~7之间，默认为5，一般不做修改
-dontusemixedcaseclassnames                # 混合时不使用大小写混合，混合后的类名为小写
-dontskipnonpubliclibraryclasses           # 指定不去忽略非公共的库类
-dontskipnonpubliclibraryclassmembers      # 指定不去忽略非公共库的类成员
-dontpreverify    # 混淆时不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度
-verbose          # 这句话能够使我们的项目混淆后产生映射文件 包含有类名->混淆后类名的映射关系
-printmapping proguardMapping.txt          # 映射文件
-printmapping bugly/BuglyUploadLog.txt          # 映射文件
-optimizations !code/simplification/cast,!field/*,!class/merging/*  # 指定混淆是采用的算法 谷歌推荐的算法，一般不做更改
-keepattributes *Annotation*,InnerClasses  # 保留Annotation不混淆
-keepattributes Signature                  # 避免混淆泛型
-keepattributes SourceFile,LineNumberTable # 抛出异常时保留代码行号
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService*
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}


# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法， 这样layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(*Event);
    void *(**On*Listener);
}

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留R下面的资源
-keep class **.R$* {
 *;
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  !static !transient <fields>;
  !private <fields>;
  !private <methods>;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

# Webview
-keepclassmembers class fqcn.of.javascript.interface.for.Webview* {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String*);
}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep class com.cf.msc.sdk.**{*;}

#------------------------------基本不用动区域 end--------------------------------------------

#TPNS SDK
-keep public class * extends android.app.Service
-keep class com.tencent.android.tpush.** {*;}
-keep class com.tencent.tpns.baseapi.** {*;}
-keep class com.tencent.tpns.mqttchannel.** {*;}
-keep class com.tencent.tpns.dataacquisition.** {*;}


#网易易盾
-keepattributes *Annotation*
-keep public class com.netease.nis.captcha.**{*;}

-keep public class android.webkit.**

-keepattributes SetJavaScriptEnabled
-keepattributes JavascriptInterface

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keep resourcexmlelements manifest/application/meta-data@value=GlideModule



# OKhttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}



# RxJava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef* {
    rx.internal.util.atomic.LinkedQueueNode* producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef* {
    rx.internal.util.atomic.LinkedQueueNode* consumerNode;
}


# EventBus 3.0.x
# http://greenrobot.org/eventbus/documentation/proguard/
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }



# Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * implements java.io.Serializable {*;}


# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# SVGA
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

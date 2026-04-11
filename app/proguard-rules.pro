# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# Kotlin
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
	public static void check*(...);
	public static void throw*(...);
}
-assumenosideeffects class java.util.Objects {
    public static ** requireNonNull(...);
}

# Strip debug log
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
}

# Activity and Fragment names
-keep class com.hayao0819.liconify.ui.activities.**
-keep class com.hayao0819.liconify.ui.fragments.**

# Xposed
-keep class de.robv.android.xposed.**
-keep class com.hayao0819.liconify.xposed.InitHook
-keepnames class com.hayao0819.liconify.xposed.**
-keepnames class com.hayao0819.liconify.xposed.utils.XPrefs
-keep class com.hayao0819.liconify.xposed.** {
    <init>(android.content.Context);
}

# Weather
-keepnames class com.hayao0819.liconify.utils.weather.**
-keep class com.hayao0819.liconify.utils.weather.** { *; }

# EventBus
-keepattributes *Annotation*
-keepclassmembers,allowoptimization,allowobfuscation class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep,allowoptimization,allowobfuscation enum org.greenrobot.eventbus.ThreadMode { *; }

# If using AsyncExecutord, keep required constructor of default event used.
# Adjust the class name if a custom failure event type is used.
-keepclassmembers,allowoptimization,allowobfuscation class org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Accessed via reflection, avoid renaming or removal
-keep,allowoptimization,allowobfuscation class org.greenrobot.eventbus.android.AndroidComponentsImpl

# Keep the ConstraintLayout Motion class
-keep,allowoptimization,allowobfuscation class androidx.constraintlayout.motion.widget.** { *; }

# Keep Recycler View Stuff
-keep,allowoptimization,allowobfuscation class androidx.recyclerview.widget.** { *; }

# Keep Parcelable Creators
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Obfuscation
-repackageclasses
-allowaccessmodification

# Root Service
-keep class com.hayao0819.liconify.services.RootProviderProxy { *; }
-keep class com.hayao0819.liconify.IRootProviderProxy { *; }

# AIDL Classes
-keep interface **.I* { *; }
-keep class **.I*$Stub { *; }
-keep class **.I*$Stub$Proxy { *; }

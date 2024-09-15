# Android-specific Rules
-keep class androidx.security.crypto.** { *; }
-keepclassmembers class androidx.security.crypto.** { *; }

# Models and API service for Retrofit and Firestore
-keepclassmembers class com.example.vocabhelper.data.models.** { *; }
-keep class com.example.vocabhelper.data.api.APIService { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

# Google Sign-In (Ensuring Google Play Services & Sign-In handling is preserved)
-keep class com.google.android.gms.auth.api.signin.** { *; }
-keep class com.google.android.gms.auth.api.signin.internal.** { *; }
-keep class com.google.android.gms.common.api.internal.** { *; }
-keep class com.google.android.gms.common.api.Status { *; }

# Prevent obfuscation of classes with the @Keep annotation
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }

# Firebase Auth & Google Play Services
-keep class com.google.firebase.auth.** { *; }
-keep class com.google.android.gms.auth.api.signin.internal.** { *; }

# Retrofit and OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn com.google.gson.stream.**

# Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep generic type information (Signature and Annotations)
-keepattributes Signature
-keepattributes *Annotation*

# Keep Retrofit Call and Response classes with generic signatures
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# OkHttp ProGuard rules
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# Avoid warnings from libraries
-dontwarn sun.misc.**
-dontwarn afu.org.checkerframework.**
-dontwarn javax.servlet.**
-dontwarn org.ietf.jgss.**

# Keep GSON TypeAdapter, Serializer, Deserializer, and Expose annotation
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepattributes Annotation

# Keep Firebase Auth classes and prevent obfuscation of FirebaseUser, FirebaseAuth, etc.
-keep class com.google.firebase.auth.FirebaseUser { *; }
-keep class com.google.firebase.auth.FirebaseAuth { *; }

# Keep Google Play Services and Firebase Auth critical classes for authentication
-keep class com.google.android.gms.common.api.internal.** { *; }
-keep class com.google.android.gms.auth.api.signin.internal.** { *; }

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.afu.org.checkerframework.checker.formatter.qual.ConversionCategory
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.afu.org.checkerframework.checker.formatter.qual.ReturnsFormat
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.afu.org.checkerframework.checker.nullness.qual.EnsuresNonNull
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.afu.org.checkerframework.checker.regex.qual.Regex
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.org.checkerframework.checker.formatter.qual.ConversionCategory
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.org.checkerframework.checker.formatter.qual.ReturnsFormat
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.org.checkerframework.checker.nullness.qual.EnsuresNonNull
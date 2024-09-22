# Android-specific Rules
-keep class androidx.security.crypto.** { *; }
-keepclassmembers class androidx.security.crypto.** { *; }

# Models and API service for Retrofit and Firestore
-keep class com.veeci.vocabhelper.data.models.** { *; }
-keep class com.veeci.vocabhelper.data.api.APIService { *; }

# Google Services (Sign-In, Firebase, Play Services)
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**
-keep class com.google.api.** { *; }

# Prevent obfuscation of classes with @Keep annotation
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * { *; }

# Retrofit and OkHttp
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn okio.**

# Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn com.google.gson.stream.**

# Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Generic type information (Signature and Annotations)
-keepattributes Signature
-keepattributes *Annotation*

# Keep GSON TypeAdapter, Serializer, Deserializer, and Expose annotation
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepattributes Annotation

# Firebase Auth & Crashlytics
-keep class com.google.firebase.auth.FirebaseUser { *; }
-keep class com.google.firebase.auth.FirebaseAuth { *; }
-dontwarn com.google.firebase.crashlytics.buildtools.reloc.**

# Retrofit Call and Response classes with generic signatures
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# Avoid warnings from other libraries
-dontwarn sun.misc.**
-dontwarn afu.org.checkerframework.**
-dontwarn javax.servlet.**
-dontwarn org.ietf.jgss.**

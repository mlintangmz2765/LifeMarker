# ProGuard rules for LifeMarker

# Keep Room related classes
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>(...);
}

# Google API Client / Apache HTTP Client warnings
# These classes are referenced by the Apache HTTP client but are not available on Android.
-dontwarn javax.naming.**
-dontwarn org.ietf.jgss.**
-dontwarn org.apache.http.**
-dontwarn com.google.api.client.extensions.android.**
-dontwarn com.google.api.client.googleapis.extensions.android.**

# Hilt/Dagger rules (usually handled by AAR but good to have)
-keepattributes *Annotation*
-keepattributes Signature

# Support for Google Drive API
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.api.client.** { *; }

# Keep Compose/Kotlin metadata if needed
-keepclassmembers class **.BuildConfig {
    public static final boolean DEBUG;
}

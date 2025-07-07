##########################################
# ✅ JANGAN OBFUSKASI CLASS UTAMA APP
-keep class com.example.smartmashroom.** { *; }

##########################################
# ✅ PERTAHANKAN ANNOTATIONS (untuk Firebase, Retrofit, dll)
-keepattributes *Annotation*

##########################################
# ✅ JANGAN OBFUSKASI MODEL DARI FIREBASE / JSON
-keep class com.google.firebase.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.squareup.moshi.** { *; }

##########################################
# ✅ RETROFIT & OKHTTP SUPPORT (jika digunakan)
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }

##########################################
# ✅ PERTAHANKAN CLASS YANG MENGGUNAKAN VIEWBINDING
-keep class **.databinding.*Binding { *; }

##########################################
# ✅ SEMBUNYIKAN STRING SENSITIF (opsional, butuh tools tambahan)
# ProGuard/R8 tidak bisa enkripsi string secara default,
# tapi ini bisa bantu sembunyikan struktur kode.
-assumenosideeffects class java.lang.String {
    public static java.lang.String format(...);
}

##########################################
# ✅ PROTEKSI UNTUK KELAS ENCODER (misal class yang kamu ubah tadi)
# -keep class com.google.android.rappor.Encoder { *; }
# -keep class com.google.android.rappor.HmacDrbg { *; }

##########################################
# ✅ JANGAN OBFUSKASI UNTUK DEBUGGING (opsional)
#-keepattributes SourceFile,LineNumberTable

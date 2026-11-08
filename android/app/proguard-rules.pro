# Keep JavaScript interface methods usable from the WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

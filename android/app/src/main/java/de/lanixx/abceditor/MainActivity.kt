package de.lanixx.abceditor

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var web: WebView
    private var pendingPermissionRequest: PermissionRequest? = null

    private val micPermLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted ->
        val req = pendingPermissionRequest
        pendingPermissionRequest = null
        if (req != null) {
            if (granted) req.grant(req.resources) else req.deny()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        web = WebView(this)
        setContentView(web)

        web.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true                 // localStorage: "Eigene Lieder"
            mediaPlaybackRequiresUserGesture = false // let click-to-play work
            allowFileAccess = true
            allowContentAccess = true
        }

        // Pass WebView microphone requests through to the Android runtime permission.
        web.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                val wantsMic = request.resources.any {
                    it == PermissionRequest.RESOURCE_AUDIO_CAPTURE
                }
                if (!wantsMic) { request.deny(); return }
                runOnUiThread {
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                        request.grant(request.resources)
                    } else {
                        pendingPermissionRequest = request
                        micPermLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            }
        }

        web.webViewClient = WebViewClient()

        // Export buttons (MIDI / WAV / ABC) produce data: or blob: URLs.
        web.setDownloadListener { url, _, _, mimetype, _ ->
            when {
                url.startsWith("data:") -> saveDataUrl(url, mimetype)
                url.startsWith("blob:") -> {
                    // Fetch the blob in JS, hand the bytes back as base64.
                    val js = """
                        (function(){
                          fetch('$url').then(r=>r.blob()).then(b=>{
                            var fr=new FileReader();
                            fr.onloadend=function(){
                              AndroidExport.save(fr.result, b.type||'$mimetype');
                            };
                            fr.readAsDataURL(b);
                          });
                        })();
                    """.trimIndent()
                    web.evaluateJavascript(js, null)
                }
            }
        }
        web.addJavascriptInterface(ExportBridge(), "AndroidExport")

        web.loadUrl("file:///android_asset/index.html")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (web.canGoBack()) web.goBack() else { isEnabled = false; onBackPressedDispatcher.onBackPressed() }
            }
        })
    }

    inner class ExportBridge {
        @JavascriptInterface
        fun save(dataUrl: String, mimetype: String) {
            runOnUiThread { saveDataUrl(dataUrl, mimetype) }
        }
    }

    private fun saveDataUrl(dataUrl: String, mimetypeHint: String) {
        try {
            val comma = dataUrl.indexOf(',')
            if (comma < 0) return
            val meta = dataUrl.substring(5, comma) // after "data:"
            val mime = meta.substringBefore(';').ifEmpty { mimetypeHint }
            val bytes = if (meta.contains("base64"))
                Base64.decode(dataUrl.substring(comma + 1), Base64.DEFAULT)
            else
                java.net.URLDecoder.decode(dataUrl.substring(comma + 1), "UTF-8").toByteArray()

            val name = "abc-editor-" + System.currentTimeMillis() + extFor(mime)
            saveBytes(name, mime, bytes)
            Toast.makeText(this, "Gespeichert: $name", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Export fehlgeschlagen: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun extFor(mime: String): String = when {
        mime.contains("midi") || mime.contains("mid") -> ".mid"
        mime.contains("wav")  -> ".wav"
        mime.contains("abc")  -> ".abc"
        mime.contains("text") -> ".abc"
        else -> ".dat"
    }

    private fun saveBytes(name: String, mime: String, bytes: ByteArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, name)
                put(MediaStore.Downloads.MIME_TYPE, if (mime.isNotEmpty()) mime else "application/octet-stream")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }
            val resolver = contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return
            resolver.openOutputStream(uri)?.use { it.write(bytes) }
            values.clear()
            values.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        } else {
            val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: filesDir
            FileOutputStream(File(dir, name)).use { it.write(bytes) }
        }
    }
}

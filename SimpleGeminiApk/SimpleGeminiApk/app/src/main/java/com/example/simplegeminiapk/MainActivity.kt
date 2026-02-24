@file:Suppress("DEPRECATION")

package com.example.simplegeminiapk

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.simplegeminiapk.MainActivity.Companion.click
import com.example.simplegeminiapk.MainActivity.Companion.textToSum
import com.example.simplegeminiapk.MainActivity.Companion.writtenText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : ComponentActivity(), LifecycleObserver {


    companion object{
        public var click = 0
        public var CLICK_KEY = "CLICK_KEY"
        public var SUM_KEY = "SUM_KEY"
        public var textToSum: String? = ""
        var writtenText: String = ""
    }

   // private lateinit var generativeModel: GenerativeModel


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(CLICK_KEY,click)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null){
            click = savedInstanceState.getInt(CLICK_KEY)
            textToSum = savedInstanceState.getString(SUM_KEY)


        }
        Log.i("SEBA",click.toString());
        lifecycle.addObserver(ApplicationObserver())

        setupKeyboardDetection(window.decorView)
        enableEdgeToEdge()
        setContent {

          //  val path = Paths.get("").toAbsolutePath().toString()
       //var path = getPath(uri,this)
      // var contentUri = FileProvider.getUriForFile(getContext())
           // getFileName(this,contentUri)

            FirstView()


        }
    }
}




@Composable
fun Modifier.simpleVerticalScrollbar(
    state: LazyListState,
    width: Dp = 8.dp
): Modifier {
    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
    val duration = if (state.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration)
    )

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (needDrawScrollbar && firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

            drawRect(
                color = Color.Red,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}

@Composable
fun FirstView() {

    val context = LocalContext.current
    val scrollState = rememberLazyListState()

    val result = remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        Log.i("SEBA",result.value.toString())
        var s = result.value?.let { uri -> getPath(uri,context) }
        Log.i("SEBA",s.toString())
       result.value = it
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .simpleVerticalScrollbar(scrollState, 8.dp),
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
       // val context = LocalContext.current

        item { Title() }
        item {TextQuery()}

        item{SimpleOutlinedTextFieldSample()}
        item{TextSum(true)}
        item{GoogleButton(result, Color.White, Color.Black, launcher, false, context)}
        item{TextSum(false)}
        item{GoogleButton(  result,Color.White, Color.Black,launcher,true, context)}

        val applicationObserver = ApplicationObserver()
        applicationObserver.onPause()

        val myApplication = MyApplication()
        myApplication.onAppBackgrounded()
        myApplication.onAppForegrounded()
    }
}

@Composable
fun TextWithSummary() {
    Text(
        text = "Please select a file to summarize: ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black,
        fontSize = 25.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 1.5.sp,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
        maxLines = 2,
        minLines = 1,
    )
}

@Composable
fun TextQuery() {
    Text(
        text = "Request to add text to summary: ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black,
        fontSize = 25.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 1.5.sp,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
        maxLines = 2,
        minLines = 1,
    )

}

@Composable
fun TextSum(bool: Boolean) {


    var text by remember { mutableStateOf("") }
    if(bool)
        text = "Please summarize the text: "
    else text ="Please select files to summarize: "


    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black,
        fontSize = 25.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 1.5.sp,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
        maxLines = 2,
        minLines = 1,
    )
}

@Composable
fun Title() {
    Text(
        text = "GeminiSimpleApk",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 40.dp),
        color = Color.Black,
        fontSize = 30.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 1.5.sp,
        // textDecoration = TextDecoration.Underline,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
        maxLines = 2,
        minLines = 1,
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            val lineCount = textLayoutResult.lineCount
            println("Line Count: $lineCount")
        },
        style = TextStyle(
            background = Color.Green,
            shadow = Shadow(color = Color.Gray, blurRadius = 40f)
        )
    )
}


@Composable
fun SimpleOutlinedTextFieldSample() {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    OutlinedTextField(
        modifier = Modifier
            .offset(x = 0.dp, y = 10.dp)
            .onFocusChanged {
                if (it.isFocused) {

                    Log.i("SEBA", "Jest")
                } else {
                    //focusManager.clearFocus()
                    Log.i("SEBA", "Nie ma")
                    focusManager.clearFocus()
                }
            },

        colors = OutlinedTextFieldDefaults.colors(
        ),
        value = text,
        onValueChange = { newValue ->
                text = newValue
            writtenText = text
            textToSum = text
            Log.i("SEBA", "Text: $text")
        },
        label = { Text("Text") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
       keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
        ),
    )
}

class ApplicationObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Log.i("ApplicationObserver", "onPause")
    }
}

fun setupKeyboardDetection(contentView: View) {
    contentView.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        contentView.getWindowVisibleDisplayFrame(r)
        val screenHeight = contentView.rootView.height
        val keypadHeight = screenHeight - r.bottom
        if (keypadHeight > screenHeight * 0.15) {

        } else {
        }
    }
}

@Composable
fun GoogleButton(
    result1: MutableState<Uri?>,
    backgroundColor: Color,
    fontColor: Color,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    bool: Boolean,
    context: Context
) {
    Button(
        onClick = {

            if(bool) {

                click++
                launcher.launch(arrayOf("image/*", "application/pdf", "text/plain"))
               // getPath(result1.value, context)
                    
            }

                 else{
                     val intent = Intent(context, SecondActivity::class.java)
                    startActivity(context, intent, null)
            }

                  },
        modifier = Modifier
            .size(100.dp)
            .shadow(5.dp),
        shape = CutCornerShape(
            topStart = 50f,
            topEnd = 50f,
            bottomEnd = 50f,
            bottomStart = 50f
        ),
        contentPadding = PaddingValues(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, // Changed from backgroundColor
            contentColor = fontColor
        ),
    ) {
        Box(
           modifier = Modifier.size(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
            }

        }
    }
}


@SuppressLint("NewApi")
fun getPath(uri: Uri,context: Context): String? {

    var intent = Intent(context, SecondActivity::class.java)
    var contentUri: Uri? = intent.data
    // check here to KITKAT or new version
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    // DocumentProvider
    if (isKitKat)  {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {

            Log.d("SEBA", "getPath: External Storage")

            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            val fullPath = getPathFromExtSD(split)

            Log.d("SEBA", "getPath: External Storage Path: $fullPath")
            return if (fullPath !== "") {
                fullPath
            } else {
                null
            }
        }


        // DownloadsProvider
        if (isDownloadsDocument(uri)) {



            Log.d("SEBA", "getPath: From Downloads")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(
                        uri,
                        arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        val fileName = cursor.getString(0)
                           val path =
                               Environment.getExternalStorageDirectory().absolutePath
                                    .toString() + "/Download/" + fileName
                                                   Log.d("SEBA", "getPath: From Download Path: $path")
                         if (!TextUtils.isEmpty(path)) {
                                return path
                            }
                    }
                } finally {
                    cursor?.close()
                }
                val id: String = DocumentsContract.getDocumentId(uri)
                Log.d("SEBA", "getPath: From Download ID: $id")
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:".toRegex(), "")
                    }
                    val contentUriPrefixesToTry =
                        arrayOf(
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads"
                        )
                    for (contentUriPrefix in contentUriPrefixesToTry) {
                        return try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse(contentUriPrefix),
                                java.lang.Long.valueOf(id)
                            )
                            getDataColumn(context, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            //In Android 8 and Android P the id is not a number
                            uri.path!!.replaceFirst("^/document/raw:".toRegex(), "")
                                .replaceFirst("^raw:".toRegex(), "")
                        }
                    }
                }
            }
            else {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                try {

                    contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                if (contentUri != null) {
                    return getDataColumn(context, contentUri!!, null, null)
                }
            }
        }


        // MediaProvider
        if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            selection = "_id=?"
            selectionArgs = arrayOf(split[1])
            return getDataColumn(
                context, contentUri, selection,
                selectionArgs
            )
        }
        if (isGoogleDriveUri(uri)) {
            return getDriveFilePath(uri,context)
        }
        if (isWhatsAppFile(uri)) {
            return getFilePathForWhatsApp(uri,context)
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context)
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                // return getFilePathFromURI(context,uri);

                var path:String?=null

                GlobalScope.launch(Dispatchers.IO) {
                    path = copyFileToInternalStorage(uri, "userfiles",context)
                }

                Thread.sleep(1000)
                path

                // return getRealPathFromURI(context,uri);
            } else {
                getDataColumn(context, uri, null, null)
            }
        }
        if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
    }
    else {
        if (isWhatsAppFile(uri)) {
            return getFilePathForWhatsApp(uri, context)
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index =
                    cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor!!.moveToFirst()) {
                    return cursor.getString(column_index!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
    }
    return null
}

private fun fileExists(filePath: String): Boolean {
    val file = File(filePath)
    return file.exists()
}

private fun getPathFromExtSD(pathData: Array<String>): String {
    val type = pathData[0]
    val relativePath = "/" + pathData[1]
    var fullPath = ""

    // on my Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
    // something like "71F8-2C0A", some kind of unique id per storage
    // don't know any API that can get the root path of that storage based on its id.
    //
    // so no "primary" type, but let the check here for other devices
    if ("primary".equals(type, ignoreCase = true)) {
        fullPath =
            Environment.getExternalStorageDirectory().toString() + relativePath
        if (fileExists(fullPath)) {
            return fullPath
        }
    }

    // Environment.isExternalStorageRemovable() is `true` for external and internal storage
    // so we cannot relay on it.
    //
    // instead, for each possible path, check if file exists
    // we'll start with secondary storage as this could be our (physically) removable sd card
    fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
    if (fileExists(fullPath)) {
        return fullPath
    }
    fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
    return if (fileExists(fullPath)) {
        fullPath
    } else fullPath
}

private fun getDriveFilePath(uri: Uri, context: Context): String? {
    val returnCursor: Cursor =
        context.contentResolver.query(uri, null, null, null, null)!!
    /*
     * Get the column indexes of the data in the Cursor,
     *     * move to the first row in the Cursor, get the data,
     *     * and display it.
     * */
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
    val file = File(context.getCacheDir(), name)
    try {
        val inputStream: InputStream? = context.getContentResolver().openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream!!.available()

        //int bufferSize = 1024;
        val bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also { read = it!! } != -1) {
            outputStream.write(buffers, 0, read)
        }
        Log.e("File Size", "Size " + file.length())
        inputStream?.close()
        outputStream.close()
        Log.e("File Path", "Path " + file.path)
        Log.e("File Size", "Size " + file.length())
    } catch (e: Exception) {
        Log.e("Exception", e.message!!)
    }
    return file.path
}

/***
 * Used for Android Q+
 * @param uri
 * @param newDirName if you want to create a directory, you can set this variable
 * @return
 */
private suspend fun copyFileToInternalStorage(
    uri: Uri,
    newDirName: String,
    context: Context
): String? {

    var returnCursor: Cursor?=null
    var path:String?=null

    Log.d(TAG, "copyFileToInternalStorage: Triggered")

    try {

        withContext(Dispatchers.IO) {
            returnCursor = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)!!
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
            returnCursor?.moveToFirst()
            val name = returnCursor?.getString(nameIndex!!)
            val size = returnCursor?.getLong(sizeIndex!!).toString()
            val output: File
            if (newDirName != "") {
                val dir = File(context.filesDir.toString() + "/" + newDirName)
                if (!dir.exists()) {
                    dir.mkdir()
                }
                output = File(context.filesDir.toString() + "/" + newDirName + "/" + name)
                Log.d(TAG, "copyFileToInternalStorage: ${output.absolutePath}")
            } else {
                output = File(context.filesDir.toString() + "/" + name)
                Log.d(TAG, "copyFileToInternalStorage: Else: ${output.absolutePath}")
            }
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(output)
                var read = 0
                val bufferSize = 1024
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers).also { read = it!! } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream?.close()
                outputStream.close()
            } catch (e: Exception) {
                Log.e("Exception", e.message!!)
            }
            path = output.path
        }

        Log.d(TAG, "copyFileToInternalStorage: Path: $path")
        return path
    } finally {
        returnCursor?.close()
    }
}

private fun getFilePathForWhatsApp(uri: Uri, context: Context): String? {
    var path:String?=null
    GlobalScope.launch(Dispatchers.IO) {
        path = copyFileToInternalStorage(uri, "whatsapp", context)
    }
    return path
}

private fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(
            uri!!, projection,
            selection, selectionArgs, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun isWhatsAppFile(uri: Uri): Boolean {
    return "com.whatsapp.provider.media" == uri.authority
}

private fun isGoogleDriveUri(uri: Uri): Boolean {
    return "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
}


fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        // Note the lowercase 'cursor' variable name
        val cursor: Cursor? =
            context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index =
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                    Log.i("SEBA",result)
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}


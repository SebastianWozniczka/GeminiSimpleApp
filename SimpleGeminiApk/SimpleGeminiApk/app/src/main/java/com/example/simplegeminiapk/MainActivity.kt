package com.example.simplegeminiapk

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MainActivity : ComponentActivity(), LifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupKeyboardDetection(window.decorView)
        enableEdgeToEdge()
        setContent {
             TextExample()
        }
    }
}
@Composable
fun TextExample() {

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        result.value = it
    }

    Column(
        modifier = Modifier.fillMaxSize(),
       horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {
        Title()
        TextQuery()
        SimpleOutlinedTextFieldSample()
        TextWithSummary()
        GoogleButton( " Please select a file to open", Color.White, Color.Black,launcher)

        var applicationObserver = ApplicationObserver()
        applicationObserver.onPause()

        var myApplication = MyApplication()
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
fun Title() {
    Text(
        text = "GeminiSimpleApk",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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

    var isVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .offset(x = 0.dp, y = 10.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    Log.i("SEBA", "Jest")
                } else {
                    //focusManager.clearFocus()
                    Log.i("SEBA", "Nie ma")
                }
            },
        value = text,
        onValueChange = { newValue ->
            // This replaces the InputFilter logic:
            // Only update the state if the new string is all letters or digits
         //   if (newValue.all { it.isLetterOrDigit() }) {
                text = newValue
           // }
        },
        label = { Text("Text") },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
       keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
        ),
       // visualTransformation = NumberCommaTransformation(),
    )

   // val etName = findViewById(R.id.etName) as EditText
    val filter: InputFilter = object : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            for (i in start..<end) {
                if (!Character.isLetterOrDigit(source.get(i))) {
                    return ""
                }
            }
            return null
        }
    }




    //text.setFilters(arrayOf<InputFilter>(filter))

    //var imageVector = ImageVector.vectorResource(R.drawable.book)

    //focusManager.clearFocus()
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
        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
            Log.i("SEBA", "ok")
        } else {
            Log.i("SEBA", "KO")
        }
    }
}

@Composable
fun GoogleButton(

    buttonText: String,
    backgroundColor: Color,
    fontColor: Color,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
) {



    Button(
        onClick = { launcher.launch(arrayOf("image/*"))  },
        modifier = Modifier
            .size(100.dp)
            .shadow(5.dp),

        //shape = RoundedCornerShape(28.dp),
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
              //  .fillMaxWidth(),
           // contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    //.fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                //Spacer(modifier = Modifier.width(4.dp))
              //  Icon(
                   // imageVector = imageVector,
                  // modifier = Modifier
                       // .size(18.dp),
                    //contentDescription = "drawable_icons",
                   // tint = Color.Unspecified
               // )
            }
            /*Text(
                modifier = Modifier.align(Alignment.Center),
                text = buttonText,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
              //  fontFamily = FontFamily(
                   // Font(
                     //   R.font.roboto_medium
                  //  )
               // )
            )*/
        }
    }
    fun etText(){
      //  val etName = findViewById(R.id.etName) as EditText
        val filter: InputFilter = object : InputFilter {
            override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                for (i in start..<end) {
                    if (!Character.isLetterOrDigit(source.get(i))) {
                        return ""
                    }
                }
                return null
            }
        }
        //etName.setFilters(arrayOf<InputFilter>(filter))
    }



}

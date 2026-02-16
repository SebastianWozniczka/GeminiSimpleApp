@file:Suppress("DEPRECATION")

package com.example.simplegeminiapk

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

class MainActivity : ComponentActivity(), LifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupKeyboardDetection(window.decorView)
        enableEdgeToEdge()
        setContent {
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

    val scrollState = rememberLazyListState()

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
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
        //item{GoogleButton(  Color.White, Color.Black,launcher,false, context )}
        item{TextSum(false)}
      //  item{GoogleButton(  Color.White, Color.Black,launcher,true, context)}

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
    backgroundColor: Color,
    fontColor: Color,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    bool: Boolean,
    context: Context
) {
    Button(
        onClick = {

            if(bool)
            launcher.launch(arrayOf("image/*","application/pdf","text/plain"))
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

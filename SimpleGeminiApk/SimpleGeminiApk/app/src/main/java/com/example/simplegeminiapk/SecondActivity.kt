package com.example.simplegeminiapk

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplegeminiapk.MainActivity.Companion.textToSum
import com.example.simplegeminiapk.MainActivity.Companion.writtenText
import com.example.simplegeminiapk.SecondActivity.Companion.pmt
import com.example.simplegeminiapk.ui.theme.SimpleGeminiApkTheme
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondActivity : ComponentActivity() {


    private lateinit var generativeModel: GenerativeModel


    companion object {
        var pmt = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleGeminiApkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
 fun Greeting(name: String, modifier: Modifier = Modifier) {

     //var generativeModel = GenerativeModel(modelName = "gemini-pro", apiKey = "AIzaSyBE41VEkCIUZWEBTlKUYG3EUiiZ5g0bAnQ")

    val generativeModel = remember {
        GenerativeModel(modelName = "gemini-1.5-flash", apiKey = "AIzaSyD7VUp60tv4wbBFdqILn28urfHnXIiq-7g")
    }

    // 2. State to hold the response text
    var responseText by remember { mutableStateOf("Loading...") }

    // 3. LaunchedEffect runs the coroutine when the Composable enters the Composition
    LaunchedEffect(Unit) {
        val prompt = "Write a short summary: $writtenText"
        try {
            // This is now inside a coroutine, so suspend functions work
            val response = generativeModel.generateContent(prompt)
            responseText = response.text ?: "No response received"
            pmt = responseText // Updating your companion object variable
        } catch (e: Exception) {
            responseText = "Error: ${e.localizedMessage}"
            Log.e("SEBA", "Error generating content", e)
        }
    }
//



    //Log.i("SEBA",response.toString())



    val context = LocalContext.current
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .simpleVerticalScrollbar(scrollState, 8.dp),
        state = scrollState,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        item { Title() }
        item { YourText() }
        item { WrittenText() }

        item {
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        }

        item { ReplyToText() }
        item { ShortenedText(responseText) }



    }
}

@Composable
fun ShortenedText(displayTxt: String){
    Text(
        text = displayTxt,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 40.dp),
        color = Color.Black,
        fontSize = 15.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 1.5.sp,
// textDecoration = TextDecoration.Underline,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp,
        overflow = TextOverflow.Ellipsis,
        softWrap = true,
       // maxLines = 2,
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
fun ReplyToText(){
    Text(
        text = "Reply to text: ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 40.dp),
        color = Color.Black,
        fontSize = 15.sp,
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
fun WrittenText(){

        var txt = writtenText
        if(txt == "")
            txt = "Empty text"

        Text(
            text = txt,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 40.dp),
            color = Color.Black,
            fontSize = 15.sp,
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
fun YourText(){
    Text(
        text = "Your text is: ",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 40.dp),
        color = Color.Black,
        fontSize = 15.sp,
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




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleGeminiApkTheme {
        Greeting("Android")
    }
}
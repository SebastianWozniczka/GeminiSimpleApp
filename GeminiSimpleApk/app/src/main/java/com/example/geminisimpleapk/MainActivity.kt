package com.example.geminisimpleapk

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.geminisimpleapk.ui.theme.GeminiSimpleApkTheme
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {



            GeminiSimpleApkTheme {

                  App()
                Greeting()

            }
        }
    }
}

public fun buttonGeminiAPI(view: View){

    val generativeModel = GenerativeModel(modelName = "gemini-pro", apiKey = "Hurry667")

    val prompt = "Write a story about a magic backpack"

    MainScope().launch {
        val response = generativeModel.generateContent(prompt)
        
    }



}

@Composable
fun Greeting() {

    val offset = Offset(5.0f, 10.0f)

    Column(modifier = Modifier.padding(40.dp,40.dp)) {
        Text(
            text = "GeminiSimpleApk",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

    }

    Column(modifier = Modifier.padding(5.dp,100.dp)){
        Text(
            text = "Please add text:",
            color = Color.Black,
            fontSize = 20.sp
        )
    }
    Column(modifier = Modifier.padding(40.dp,150.dp)){
        var text by rememberSaveable { mutableStateOf("Text") }

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text("Label") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),maxLines = 2, textStyle = TextStyle(fontSize = 20.sp,color = Color.Blue, fontWeight = FontWeight.Bold), modifier = Modifier.padding(20.dp)

        )
    }



}

@Composable
fun App() {
    var text by remember { mutableStateOf("Initial Text") }

    Column(modifier = Modifier.padding(100.dp,200.dp)) {
        Text(text)
        Button(onClick = { text += "" }) {
            Text("Start")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        Column (Modifier.padding(40.dp,200.dp)){
            Text("S")
        }

}
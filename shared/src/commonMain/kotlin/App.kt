import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        var tts by remember { mutableStateOf(false) }
        var image by remember { mutableStateOf<ImageBitmap?>(null) }
        var showWebView by remember { mutableStateOf(false) }
        var showPdfInWebView by remember { mutableStateOf(false) }

        LaunchedEffect(tts) {
            if (tts) {
                textToSpeech("Hello!")
            }
        }

        LaunchedEffect(Unit) {
            image = urlToBitmap(
                "https://image.pitchbook.com/WevrNAvMz4QxF1Ua1HVLjRiP1fV1676357410021_200x200"
            )
        }

        if (showWebView) {
            WebViewScreen(
                url = "https://developer.apple.com/documentation/webkit/wkwebview",
                onBackPressed = {
                    showWebView = !showWebView
                })
        } else if (showPdfInWebView) {
            WebViewScreen(
                url = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf",
                onBackPressed = {
                    showPdfInWebView = !showPdfInWebView
                })
        } else {
            LazyColumn(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(15.dp)
            ) {

                item {
                    Text(
                        text = "MapView",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    MapView(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        latLong = LatLong(name = "Chennai", lat = 13.067439, long = 80.237617)
                    )

                    Divider()
                }

                item {
                    Text(
                        text = "UI Component Button",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

//                    UIComponentButton(
//                        modifier = Modifier.width(120.dp).height(50.dp),
//                        text = "Click Me",
//                        onClick = {
//                            println("Clicked Me")
//                        })

                    UIComponentButton(onClick = {
                        println("Clicked Me")
                    }).UIComponentButton(
                        modifier = Modifier.width(120.dp).height(50.dp),
                        text = "Click Me"
                    )

                    Divider()
                }

                item {
                    Text(
                        text = "UI Component Text",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    UIComponentText(
                        modifier = Modifier.width(120.dp).height(50.dp),
                        text = "hello world"
                    )

                    Divider()
                }

                item {
                    Text(
                        text = "Pdf in WebView",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    Button(onClick = {
                        showPdfInWebView = !showPdfInWebView
                    }) {
                        Text("Pdf - ${if (showWebView) "HIDE" else "SHOW"}")
                    }

                    Divider()
                }

                item {
                    Text(
                        text = "WebView",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    Button(onClick = {
                        showWebView = !showWebView
                    }) {
                        Text("WebView - ${if (showWebView) "HIDE" else "SHOW"}")
                    }

                    Divider()
                }

                item {
                    Text(
                        text = "Text To Speech",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    Button(onClick = {
                        tts = !tts
                    }) {
                        Text("TTS - ${if (tts) "STOP" else "PLAY"}")
                    }
                    Divider()
                }

                item {
                    Text(
                        text = "Image from URL",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    if (image != null) {
                        Image(bitmap = image!!, contentDescription = null)
                    }
                    Divider()
                }

                item {
                    Text(
                        text = "Audio from URL",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    MediaPlayerUI(
                        modifier = Modifier.height(200.dp).fillMaxWidth(),
                        url = "https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"
                    )
                    Divider()
                }

                item {
                    Text(
                        text = "Video from URL",
                        style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                    )

                    MediaPlayerUI(
                        modifier = Modifier.height(200.dp).fillMaxWidth(),
                        url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"
                    )
                    Divider()
                }

            }
        }
    }
}

@Composable
fun WebViewScreen(
    url: String,
    onBackPressed: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = {
                onBackPressed()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }, title = {})
    }) {
        WebViewUI(
            modifier = Modifier.fillMaxSize(),
            url = url
        )
    }
}

expect fun getPlatformName(): String
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance
import platform.AVFoundation.AVLayerVideoGravityResize
import platform.AVFoundation.AVPlayer
import platform.AVKit.AVPlayerViewController
import platform.CoreGraphics.CGRectZero
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationDegrees
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UILabel
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(modifier: Modifier, latLong: LatLong) {
    var mkMapView by remember { mutableStateOf<MKMapView?>(null) }
    var mkPointAnnotation by remember { mutableStateOf<MKPointAnnotation?>(null) }

    LaunchedEffect(Unit) {
        mkPointAnnotation = withContext(Dispatchers.Main) {
            MKPointAnnotation().apply {
                setTitle(latLong.name)
                setCoordinate(
                    coordinate = CLLocationCoordinate2DMake(
                        latitude = latLong.lat,
                        longitude = latLong.long
                    )
                )
            }
        }

        mkMapView = withContext(Dispatchers.Main) {
            MKMapView()
        }

    }

    if (mkMapView != null && mkPointAnnotation != null) {
        UIKitView(
            modifier = modifier,
            factory = {
                mkMapView!!.apply {
                    addAnnotation(annotation = mkPointAnnotation!!)
                }
            }
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UIComponentButton(modifier: Modifier, text: String, onClick: () -> Unit) {
    var uiButton by remember { mutableStateOf<UIButton?>(null) }
    LaunchedEffect(Unit) {
        uiButton = withContext(Dispatchers.Main) {
            UIButton.buttonWithType(buttonType = UIButtonTypeSystem).apply {
                setTitle(title = text, forState = UIControlStateNormal)
//                addTarget(
//                    target = object : NSObject(), UIGestureRecognizerDelegateProtocol {
//                        override fun gestureRecognizerShouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
//                            onClick()
//                            return true
//                        }
//                    },
//                    action = NSSelectorFromString("invoke"),
//                    forControlEvents = UIControlEventTouchUpInside
//                )
                addTarget(
                    this,
                    NSSelectorFromString("event_button_click"),
                    UIControlEventTouchUpInside
                )
            }
        }
    }

    if (uiButton != null) {
        UIKitView(modifier = modifier,
            factory = {
                uiButton!!
            })
    }
}


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun UIComponentText(modifier: Modifier, text: String) {
    UIKitView(modifier = modifier,
        factory = {
            val uiLabel = UILabel()
            uiLabel.setText(text = text)
            uiLabel
        })
}

actual suspend fun textToSpeech(text: String) {
    try {
        val voice = withContext(Dispatchers.Main) {
            AVSpeechSynthesisVoice.voiceWithLanguage(languageCode = "en-GB")
        }

        val utterance = withContext(Dispatchers.Main) {
            AVSpeechUtterance(string = text).apply {
                // Configure the utterance.
                rate = 0.57f
                pitchMultiplier = 0.8f
                postUtteranceDelay = 0.2
                volume = 0.8f
                setVoice(voice = voice)
            }
        }

        val synthesizer = withContext(Dispatchers.Main) {
            AVSpeechSynthesizer()
        }

        synthesizer.speakUtterance(utterance = utterance)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

actual suspend fun urlToBitmap(url: String): ImageBitmap {
    return withContext(Dispatchers.Default) {
        NSURL(string = url).readBytes().toImageBitmap()
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MediaPlayerUI(modifier: Modifier, url: String) {
    var avPlayer by remember { mutableStateOf<AVPlayer?>(null) }
    var avPlayerViewController by remember { mutableStateOf<AVPlayerViewController?>(null) }

    LaunchedEffect(Unit) {
        avPlayer = withContext(Dispatchers.Main) {
            AVPlayer(uRL = NSURL(string = url))
        }
        avPlayerViewController = withContext(Dispatchers.Main) {
            AVPlayerViewController().apply {
                player = avPlayer
                showsPlaybackControls = true
                entersFullScreenWhenPlaybackBegins = true
                exitsFullScreenWhenPlaybackEnds = true
                videoGravity = AVLayerVideoGravityResize
                contentOverlayView?.autoresizesSubviews = true
            }
        }
    }

    if (avPlayer != null && avPlayerViewController != null) {
        UIKitView(
            modifier = modifier,
            factory = {
                avPlayerViewController?.view!!
            },
            interactive = true,
            background = Color.Gray
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewUI(modifier: Modifier, url: String) {
    var wkWebView by remember { mutableStateOf<WKWebView?>(null) }

    LaunchedEffect(Unit) {
        val webConfiguration = withContext(Dispatchers.Main) {
            WKWebViewConfiguration()
        }
        wkWebView = withContext(Dispatchers.Main) {
            WKWebView(frame = CGRectZero.readValue(), configuration = webConfiguration)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (wkWebView != null) {
            UIKitView(
                modifier = modifier,
                factory = {
                    wkWebView!!
                },
                update = {
                    val request = NSURLRequest(uRL = NSURL(string = url))
                    wkWebView?.loadRequest(request)
                }
            )
        } else {
            CircularProgressIndicator()
        }
    }

}
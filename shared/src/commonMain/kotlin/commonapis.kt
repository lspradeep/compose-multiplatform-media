import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun MapView(modifier: Modifier, latLong: LatLong)

data class LatLong(
    var name: String,
    var lat: Double,
    var long: Double
)

@Composable
expect fun UIComponentButton(modifier: Modifier, text: String, onClick: () -> Unit)

@Composable
expect fun UIComponentText(modifier: Modifier, text: String)
expect suspend fun textToSpeech(text: String)

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect suspend fun urlToBitmap(url: String): ImageBitmap

@Composable
expect fun MediaPlayerUI(modifier: Modifier, url: String)

@Composable
expect fun WebViewUI(modifier: Modifier, url: String)
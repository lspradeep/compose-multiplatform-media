import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
actual fun MapView(modifier: Modifier, latLong: LatLong){

}
@Composable
actual fun UIComponentButton(modifier: Modifier,text: String, onClick: () -> Unit){

}

@Composable
actual fun UIComponentText(modifier: Modifier,text: String){

}

actual suspend fun textToSpeech(text: String) {

}

actual fun ByteArray.toImageBitmap(): ImageBitmap = toAndroidBitmap().asImageBitmap()

fun ByteArray.toAndroidBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

actual suspend fun urlToBitmap(url: String): ImageBitmap {
    return ImageBitmap(0, 0)
}

@Composable
actual fun MediaPlayerUI(modifier: Modifier, url: String) {

}

@Composable
actual fun WebViewUI(modifier: Modifier, url: String){

}
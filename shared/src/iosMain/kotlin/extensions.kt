import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.yield
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
suspend fun NSURL.readBytes(): ByteArray =
    with(readData()) {
        ByteArray(length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), bytes, length)
            }
        }
    }

suspend fun NSURL.readData(): NSData {
    while (true) {
        val data = NSData.dataWithContentsOfURL(this)
        if (data != null)
            return data
        yield()
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap =
    Image.makeFromEncoded(this).toComposeImageBitmap()
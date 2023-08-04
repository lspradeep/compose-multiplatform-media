import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIButton
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UIGestureRecognizerDelegateProtocol
import platform.darwin.NSObject

actual class UIComponentButton actual constructor(val onClick: () -> Unit) {
    @OptIn(ExperimentalForeignApi::class)
    @Composable
    actual fun UIComponentButton(modifier: Modifier, text: String) {
        var uiButton by remember { mutableStateOf<UIButton?>(null) }
        LaunchedEffect(Unit) {
            uiButton = withContext(Dispatchers.Main) {
                UIButton.buttonWithType(buttonType = UIButtonTypeSystem).apply {
                    setTitle(title = text, forState = UIControlStateNormal)
                    addTarget(
                        target = object : NSObject(), UIGestureRecognizerDelegateProtocol {
                            override fun gestureRecognizerShouldBegin(gestureRecognizer: UIGestureRecognizer): Boolean {
                                onClick()
                                return true
                            }
                        },
                        action = NSSelectorFromString("onClickInternal"),
                        forControlEvents = UIControlEventTouchUpInside
                    )
//                    addTarget(
//                        this,
//                        NSSelectorFromString("onClickInternal"),
//                        UIControlEventTouchUpInside
//                    )
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

    @ObjCAction
    fun onClickInternal() {
        onClick()
    }
}
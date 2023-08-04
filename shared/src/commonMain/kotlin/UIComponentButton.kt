import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect class UIComponentButton(onClick: () -> Unit){
    @Composable
    fun UIComponentButton(modifier: Modifier, text: String)
}
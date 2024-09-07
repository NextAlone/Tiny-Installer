package dev.trindadedev.tinyinstaller.ui.page.installer.dialog.inner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import dev.trindadedev.tinyinstaller.ui.page.installer.dialog.DialogInnerParams

@Composable
fun DialogButtons(
    id: String, content: (@Composable () -> List<DialogButton>)
) = DialogInnerParams(id) {
    val buttons = content.invoke()
    Column(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val single = if (buttons.size > 2) buttons.size % 2 else buttons.size
        for (i in 0 until single) {
            InnerButton(buttons[i])
        }
        for (i in single until buttons.size step 2) {
            Box {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    buttons[i].let {
                        InnerButton(
                            it, Modifier.weight(it.weight)
                        )
                    }
                    buttons[i + 1].let {
                        InnerButton(
                            it, Modifier.weight(it.weight)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun InnerButton(
    button: DialogButton, modifier: Modifier = Modifier
) {
    TextButton(
        button.onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(button.text)
    }
}
package dev.trindadedev.tinyinstaller.ui.page.installer.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

import dev.trindadedev.tinyinstaller.data.installer.repo.InstallerRepo
import dev.trindadedev.tinyinstaller.ui.widget.dialog.PositionDialog

import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DialogPage(
    installer: InstallerRepo, viewModel: DialogViewModel = getViewModel {
        parametersOf(installer)
    }
) {
    LaunchedEffect(installer.id) {
        viewModel.dispatch(DialogViewAction.CollectRepo(installer))
    }
    val params = DialogGenerateParams(installer, viewModel)

    PositionDialog(
        onDismissRequest = {
            viewModel.dispatch(DialogViewAction.Background)
        },
        modifier = Modifier.animateContentSize(),
        centerIcon = dialogInnerWidget(installer, params.icon),
        centerTitle = dialogInnerWidget(installer, params.title),
        centerSubtitle = dialogInnerWidget(installer, params.subtitle),
        centerText = dialogInnerWidget(installer, params.text),
        centerContent = dialogInnerWidget(installer, params.content),
        centerButton = dialogInnerWidget(installer, params.buttons)
    )
}
package dev.trindadedev.tinyinstaller.ui.page.installer.dialog.inner

import android.content.Intent
import android.net.Uri
import android.provider.Settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

import dev.trindadedev.tinyinstaller.R
import dev.trindadedev.tinyinstaller.data.installer.repo.InstallerRepo
import dev.trindadedev.tinyinstaller.ui.page.installer.dialog.*

@Composable
fun InstallFailedDialog(
    installer: InstallerRepo, viewModel: DialogViewModel
): DialogParams {
    val context = LocalContext.current
    val packageName =
        installer.entities.filter { it.selected }.map { it.app }.first().packageName
    return InstallInfoDialog(installer, viewModel) {
        context.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", packageName, null))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }.copy(text = DialogInnerParams(
        DialogParamsType.InstallerInstallFailed.id, errorText(installer, viewModel)
    ), buttons = DialogButtons(
        DialogParamsType.InstallerInstallFailed.id
    ) {
        listOf(DialogButton(stringResource(R.string.previous)) {
            viewModel.dispatch(DialogViewAction.InstallPrepare)
        }, DialogButton(stringResource(R.string.cancel)) {
            viewModel.dispatch(DialogViewAction.Close)
        })
    })
}
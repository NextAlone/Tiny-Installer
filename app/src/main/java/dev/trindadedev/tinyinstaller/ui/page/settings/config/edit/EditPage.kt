package dev.trindadedev.tinyinstaller.ui.page.settings.config.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import dev.trindadedev.tinyinstaller.R
import dev.trindadedev.tinyinstaller.data.settings.model.room.entity.ConfigEntity
import dev.trindadedev.tinyinstaller.data.settings.util.ConfigUtil
import dev.trindadedev.tinyinstaller.ui.theme.none
import dev.trindadedev.tinyinstaller.ui.widget.setting.DropDownMenuWidget
import dev.trindadedev.tinyinstaller.ui.widget.setting.LabelWidget
import dev.trindadedev.tinyinstaller.ui.widget.setting.SwitchWidget

import kotlinx.coroutines.flow.collectLatest

import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

import kotlin.math.absoluteValue

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Composable
fun EditPage(
    navController: NavController,
    id: Long? = null,
    viewModel: EditViewModel = getViewModel { parametersOf(id) }
) {
    LaunchedEffect(true) {
        viewModel.dispatch(EditViewAction.Init)
    }

    val showFloatingState = remember {
        mutableStateOf(true)
    }
    val showFloating by showFloatingState

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditViewEvent.SnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true,
                    )
                }

                is EditViewEvent.Saved -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .nestedScroll(ShowFloatingActionButtonNestedScrollConnection(showFloatingState)),
        contentWindowInsets = WindowInsets.none,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = if (id == null) R.string.add else R.string.update))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.TwoTone.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.back
                            )
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFloating,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                val text = stringResource(R.string.save)
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.TwoTone.Save,
                            contentDescription = text
                        )
                    },
                    text = {
                        Text(text)
                    },
                    onClick = {
                        viewModel.dispatch(EditViewAction.SaveData)
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) {
        // TO:DO format code.
        ElevatedCard {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .padding(it),
        ) {
            item {
                DataNameWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataDescriptionWidget(viewModel = viewModel)
                divider()
            }
            item {
                LabelWidget(label = stringResource(id = R.string.more))
                divider()
            }
            item {
                DataAuthorizerWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataCustomizeAuthorizerWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataInstallModeWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataDeclareInstallerWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataInstallerWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataForAllUserWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataAllowTestOnlyWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataAllowDowngradeWidget(viewModel = viewModel)
                divider()
            }
            item {
                DataAutoDeleteWidget(viewModel = viewModel)
            }
        }
        }
    }
}

class ShowFloatingActionButtonNestedScrollConnection(
    private val showFloatingState: MutableState<Boolean>
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y.absoluteValue > 1) showFloatingState.value = available.y >= 0
        return super.onPreScroll(available, source)
    }
}

@Composable
fun divider() {
    Divider(
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataNameWidget(viewModel: EditViewModel) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .focusable(),
        leadingIcon = {
            Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
        },
        label = {
            Text(text = stringResource(id = R.string.config_name))
        },
        value = viewModel.state.data.name,
        onValueChange = {
            viewModel.dispatch(EditViewAction.ChangeDataName(it))
        },
        singleLine = true,
        isError = viewModel.state.data.errorName
    )
}

@Composable
fun DataDescriptionWidget(viewModel: EditViewModel) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .focusable(),
        leadingIcon = {
            Icon(imageVector = Icons.TwoTone.More, contentDescription = null)
        },
        label = {
            Text(text = stringResource(id = R.string.config_description))
        },
        value = viewModel.state.data.description,
        onValueChange = { viewModel.dispatch(EditViewAction.ChangeDataDescription(it)) },
        maxLines = 8,
    )
}

@Composable
fun DataAuthorizerWidget(viewModel: EditViewModel) {
    val authorizer = viewModel.state.data.authorizer
    val data = mapOf(
        ConfigEntity.Authorizer.Global to stringResource(R.string.config_authorizer_global),
        ConfigEntity.Authorizer.None to stringResource(R.string.config_authorizer_none),
        ConfigEntity.Authorizer.Root to stringResource(R.string.config_authorizer_root),
        ConfigEntity.Authorizer.Shizuku to stringResource(R.string.config_authorizer_shizuku),
        ConfigEntity.Authorizer.Dhizuku to stringResource(R.string.config_authorizer_dhizuku),
        ConfigEntity.Authorizer.Customize to stringResource(R.string.config_authorizer_customize)
    )
    DropDownMenuWidget(
        icon = Icons.TwoTone.Memory,
        title = stringResource(R.string.config_authorizer),
        description = if (data.containsKey(authorizer)) data[authorizer] else null,
        choice = data.keys.toList().indexOf(authorizer),
        data = data.values.toList(),
    ) {
        data.keys.toList().getOrNull(it)?.let {
            viewModel.dispatch(EditViewAction.ChangeDataAuthorizer(it))
        }
    }
}

@Composable
fun DataCustomizeAuthorizerWidget(viewModel: EditViewModel) {
    if (!viewModel.state.data.authorizerCustomize) return
    val customizeAuthorizer = viewModel.state.data.customizeAuthorizer
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .focusable(),
        leadingIcon = {
            Icon(imageVector = Icons.TwoTone.Terminal, contentDescription = null)
        },
        label = {
            Text(text = stringResource(R.string.config_customize_authorizer))
        },
        value = customizeAuthorizer,
        onValueChange = { viewModel.dispatch(EditViewAction.ChangeDataCustomizeAuthorizer(it)) },
        maxLines = 8,
        isError = viewModel.state.data.errorCustomizeAuthorizer
    )
}

@Composable
fun DataInstallModeWidget(viewModel: EditViewModel) {
    val installMode = viewModel.state.data.installMode
    val data = mapOf(
        ConfigEntity.InstallMode.Global to stringResource(R.string.config_install_mode_global),
        ConfigEntity.InstallMode.Dialog to stringResource(R.string.config_install_mode_dialog),
        ConfigEntity.InstallMode.AutoDialog to stringResource(R.string.config_install_mode_auto_dialog),
        ConfigEntity.InstallMode.Notification to stringResource(R.string.config_install_mode_notification),
        ConfigEntity.InstallMode.AutoNotification to stringResource(R.string.config_install_mode_auto_notification),
        ConfigEntity.InstallMode.Ignore to stringResource(R.string.config_install_mode_ignore),
    )
    DropDownMenuWidget(
        icon = Icons.TwoTone.Downloading,
        title = stringResource(R.string.config_install_mode),
        description = if (data.containsKey(installMode)) data[installMode] else null,
        choice = data.keys.toList().indexOf(installMode),
        data = data.values.toList(),
    ) {
        data.keys.toList().getOrNull(it)?.let {
            viewModel.dispatch(EditViewAction.ChangeDataInstallMode(it))
        }
    }
}

@Composable
fun DataDeclareInstallerWidget(viewModel: EditViewModel) {
    var authorizer = viewModel.state.data.authorizer
    if (authorizer == ConfigEntity.Authorizer.Global)
        authorizer = ConfigUtil.globalAuthorizer
    val declareInstaller = viewModel.state.data.declareInstaller
    if (authorizer == ConfigEntity.Authorizer.Dhizuku) {
        viewModel.dispatch(EditViewAction.ChangeDataDeclareInstaller(false))
        return
    }
    SwitchWidget(
        icon = Icons.TwoTone.Face,
        title = stringResource(id = R.string.config_declare_installer),
        checked = declareInstaller
    ) {
        viewModel.dispatch(EditViewAction.ChangeDataDeclareInstaller(it))
    }
}

@Composable
fun DataInstallerWidget(viewModel: EditViewModel) {
    val declareInstaller = viewModel.state.data.declareInstaller
    val installer = viewModel.state.data.installer
    if (!declareInstaller) return
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .focusable(),
        leadingIcon = {
            Icon(imageVector = Icons.TwoTone.PsychologyAlt, contentDescription = null)
        },
        label = {
            Text(text = stringResource(id = R.string.config_installer))
        },
        value = installer,
        onValueChange = {
            viewModel.dispatch(EditViewAction.ChangeDataInstaller(it))
        },
        singleLine = true,
        isError = viewModel.state.data.errorInstaller
    )
}

@Composable
fun DataForAllUserWidget(viewModel: EditViewModel) {
    SwitchWidget(
        icon = Icons.TwoTone.People,
        title = stringResource(id = R.string.config_for_all_user),
        checked = viewModel.state.data.forAllUser
    ) {
        viewModel.dispatch(EditViewAction.ChangeDataForAllUser(it))
    }
}

@Composable
fun DataAllowTestOnlyWidget(viewModel: EditViewModel) {
    SwitchWidget(
        icon = Icons.TwoTone.BugReport,
        title = stringResource(id = R.string.config_allow_test_only),
        description = stringResource(id = R.string.config_allow_test_only_dsp),
        checked = viewModel.state.data.allowTestOnly,
        onCheckedChange = {
            viewModel.dispatch(EditViewAction.ChangeDataAllowTestOnly(it))
        }
    )
}

@Composable
fun DataAllowDowngradeWidget(viewModel: EditViewModel) {
    SwitchWidget(
        icon = Icons.TwoTone.TrendingDown,
        title = stringResource(id = R.string.config_allow_downgrade),
        description = stringResource(id = R.string.config_allow_downgrade_dsp),
        checked = viewModel.state.data.allowDowngrade,
        onCheckedChange = {
            viewModel.dispatch(EditViewAction.ChangeDataAllowDowngrade(it))
        }
    )
}

@Composable
fun DataAutoDeleteWidget(viewModel: EditViewModel) {
    SwitchWidget(
        icon = Icons.TwoTone.Delete,
        title = stringResource(id = R.string.config_auto_delete),
        description = stringResource(id = R.string.config_auto_delete_dsp),
        checked = viewModel.state.data.autoDelete,
        onCheckedChange = {
            viewModel.dispatch(EditViewAction.ChangeDataAutoDelete(it))
        }
    )
}







package com.lifemarker.ui.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifemarker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val localeList = AppCompatDelegate.getApplicationLocales()
    val currentLocale = if (!localeList.isEmpty) localeList[0]?.toLanguageTag() ?: "id" else "id"

    var isBackupAction by remember { mutableStateOf(true) }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.handleSignInResult(context, isBackupAction, result.data)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchBackupInfo(context)
    }

    LaunchedEffect(uiState.syncStatusMessageResId) {
        uiState.syncStatusMessageResId?.let { resId ->
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
            viewModel.clearSyncMessage()
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            ListItem(
                headlineContent = { Text(stringResource(R.string.language)) },
                supportingContent = { Text(stringResource(R.string.select_language)) },
                leadingContent = {
                    Icon(Icons.Default.Language, contentDescription = null)
                }
            )
            
            LanguageOption(
                title = "Bahasa Indonesia",
                isSelected = currentLocale.startsWith("id") || currentLocale.startsWith("in"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("id"))
                }
            )
            
            LanguageOption(
                title = "English",
                isSelected = currentLocale.startsWith("en"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                }
            )

            LanguageOption(
                title = "العربية (Arabic)",
                isSelected = currentLocale.startsWith("ar"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"))
                }
            )

            LanguageOption(
                title = "中文 (Chinese)",
                isSelected = currentLocale.startsWith("zh"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("zh"))
                }
            )

            LanguageOption(
                title = "Français (French)",
                isSelected = currentLocale.startsWith("fr"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fr"))
                }
            )

            LanguageOption(
                title = "Русский (Russian)",
                isSelected = currentLocale.startsWith("ru"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ru"))
                }
            )

            LanguageOption(
                title = "Español (Spanish)",
                isSelected = currentLocale.startsWith("es"),
                onClick = {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(16.dp))

            ListItem(
                headlineContent = { Text(stringResource(R.string.google_drive_sync)) },
                supportingContent = { 
                    val lastSync = if (uiState.lastBackupTime != null) {
                        val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(uiState.lastBackupTime!!))
                        stringResource(R.string.last_sync, date)
                    } else {
                        stringResource(R.string.no_backup)
                    }
                    Text(text = lastSync)
                },
                leadingContent = {
                    Icon(Icons.Default.Sync, contentDescription = null)
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        isBackupAction = true
                        googleSignInLauncher.launch(viewModel.getGoogleSignInIntent())
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isSyncing
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.backup))
                }
                
                Button(
                    onClick = {
                        isBackupAction = false
                        googleSignInLauncher.launch(viewModel.getGoogleSignInIntent())
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isSyncing
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.restore))
                }
            }

            if (uiState.isSyncing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (uiState.needsRestart) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text(stringResource(R.string.sync_success)) },
                    text = { Text(stringResource(R.string.restart_required)) },
                    confirmButton = {
                        TextButton(onClick = { 
                        }) {
                            Text(text = "OK")
                        }
                    }
                )
            }

            val uriHandler = LocalUriHandler.current

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { uriHandler.openUri("https://github.com/mlintangmz2765/LifeMarker") }
                ) {
                    Icon(
                        imageVector = ImageVector.Builder(
                            name = "github",
                            defaultWidth = 24.dp,
                            defaultHeight = 24.dp,
                            viewportWidth = 24f,
                            viewportHeight = 24f
                        ).path(fill = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)) {
                            moveTo(12f, 0.297f)
                            curveTo(5.37f, 0.297f, 0f, 5.67f, 0f, 12.297f)
                            curveTo(0f, 17.597f, 3.438f, 22.097f, 8.205f, 23.682f)
                            curveTo(8.805f, 23.792f, 9.025f, 23.424f, 9.025f, 23.105f)
                            curveTo(9.025f, 22.82f, 9.015f, 22.065f, 9.01f, 21.065f)
                            curveTo(5.672f, 21.789f, 4.968f, 19.453f, 4.968f, 19.453f)
                            curveTo(4.425f, 18.077f, 3.633f, 17.7f, 3.633f, 17.7f)
                            curveTo(2.546f, 16.956f, 3.717f, 16.971f, 3.717f, 16.971f)
                            curveTo(4.922f, 17.055f, 5.555f, 18.207f, 5.555f, 18.207f)
                            curveTo(6.625f, 20.042f, 8.364f, 19.512f, 9.05f, 19.205f)
                            curveTo(9.158f, 18.429f, 9.467f, 17.9f, 9.81f, 17.6f)
                            curveTo(7.145f, 17.3f, 4.344f, 16.268f, 4.344f, 11.67f)
                            curveTo(4.344f, 10.36f, 4.809f, 9.29f, 5.579f, 8.45f)
                            curveTo(5.444f, 8.147f, 5.039f, 6.927f, 5.684f, 5.274f)
                            curveTo(5.684f, 5.274f, 6.689f, 4.952f, 8.984f, 6.504f)
                            curveTo(9.944f, 6.237f, 10.964f, 6.105f, 11.984f, 6.099f)
                            curveTo(13.004f, 6.105f, 14.024f, 6.237f, 14.984f, 6.504f)
                            curveTo(17.279f, 4.952f, 18.284f, 5.274f, 18.284f, 5.274f)
                            curveTo(18.929f, 6.927f, 18.524f, 8.147f, 18.389f, 8.45f)
                            curveTo(19.159f, 9.29f, 19.624f, 10.36f, 19.624f, 11.67f)
                            curveTo(19.624f, 16.278f, 16.823f, 17.297f, 14.158f, 17.59f)
                            curveTo(14.578f, 17.95f, 14.968f, 18.686f, 14.968f, 19.81f)
                            curveTo(14.968f, 21.416f, 14.953f, 22.706f, 14.953f, 23.096f)
                            curveTo(14.953f, 23.411f, 15.163f, 23.786f, 15.778f, 23.666f)
                            curveTo(20.545f, 22.081f, 23.984f, 17.581f, 23.984f, 12.286f)
                            curveTo(23.984f, 5.656f, 18.614f, 0.286f, 11.984f, 0.286f)
                            close()
                        }.build(),
                        contentDescription = "GitHub",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = stringResource(R.string.copyright),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LanguageOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
        }
    }
}

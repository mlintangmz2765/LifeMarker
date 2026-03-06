package com.lifemarker.ui.settings

import android.app.Activity
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.lifemarker.sync.GoogleDriveManager
import com.lifemarker.sync.GoogleDriveService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val isSyncing: Boolean = false,
    val syncStatusMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val driveService: GoogleDriveService,
    private val driveManager: GoogleDriveManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun getGoogleSignInIntent() = driveService.getSignInIntent()

    fun handleSignInResult(context: Context, backup: Boolean, intent: android.content.Intent?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, syncStatusMessage = "Authenticating...") }
            
            val account = driveService.getSignedInAccountFromIntent(intent)
            if (account == null) {
                _uiState.update { it.copy(isSyncing = false, syncStatusMessage = "Authentication failed") }
                return@launch
            }

            _uiState.update { it.copy(syncStatusMessage = if (backup) "Backing up data..." else "Restoring data...") }
            
            val result = if (backup) {
                driveManager.backupDatabase(account)
            } else {
                driveManager.restoreDatabase(account)
            }

            result.fold(
                onSuccess = { msg ->
                    _uiState.update { it.copy(isSyncing = false, syncStatusMessage = msg) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(isSyncing = false, syncStatusMessage = err.message ?: "Sync failed") }
                }
            )
        }
    }

    fun clearSyncMessage() {
        _uiState.update { it.copy(syncStatusMessage = null) }
    }
}

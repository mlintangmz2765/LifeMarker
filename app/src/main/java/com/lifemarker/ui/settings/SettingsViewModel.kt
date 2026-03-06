package com.lifemarker.ui.settings

import com.lifemarker.R

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
    val syncStatusMessageResId: Int? = null,
    val lastBackupTime: Long? = null,
    val needsRestart: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val driveService: GoogleDriveService,
    private val driveManager: GoogleDriveManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
    }


    fun getGoogleSignInIntent() = driveService.getSignInIntent()

    fun handleSignInResult(context: Context, backup: Boolean, intent: android.content.Intent?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, syncStatusMessageResId = R.string.authenticating) }
            
            driveService.getSignedInAccountFromIntent(intent).fold(
                onSuccess = { account ->
                    _uiState.update { it.copy(syncStatusMessageResId = if (backup) R.string.backing_up else R.string.restoring) }
                    
                    val result = if (backup) {
                        driveManager.backupDatabase(account)
                    } else {
                        driveManager.restoreDatabase(account)
                    }

                    result.fold(
                        onSuccess = {
                            _uiState.update { it.copy(
                                isSyncing = false, 
                                syncStatusMessageResId = R.string.sync_success,
                                needsRestart = !backup
                            ) }
                            fetchBackupInfo(account)
                        },
                        onFailure = { err ->
                            _uiState.update { it.copy(isSyncing = false, syncStatusMessageResId = R.string.sync_failed) }
                        }
                    )
                },
                onFailure = { 
                    _uiState.update { it.copy(isSyncing = false, syncStatusMessageResId = R.string.auth_failed) }
                }
            )
        }
    }

    fun fetchBackupInfo(context: Context) {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            fetchBackupInfo(account)
        }
    }

    private fun fetchBackupInfo(account: com.google.android.gms.auth.api.signin.GoogleSignInAccount) {
        viewModelScope.launch {
            val info = driveManager.getBackupInfo(account)
            _uiState.update { it.copy(lastBackupTime = info?.modifiedTime) }
        }
    }

    fun clearSyncMessage() {
        _uiState.update { it.copy(syncStatusMessageResId = null) }
    }
}

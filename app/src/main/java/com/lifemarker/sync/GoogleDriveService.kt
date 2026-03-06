package com.lifemarker.sync

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.lifemarker.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.WEB_CLIENT_ID)
        .requestScopes(Scope(DriveScopes.DRIVE_FILE))
        .build()

    private val signInClient: GoogleSignInClient = GoogleSignIn.getClient(context, signInOptions)

    fun getSignInIntent(): Intent = signInClient.signInIntent

    fun getSignedInAccountFromIntent(intent: Intent?): Result<GoogleSignInAccount> {
        return try {
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)
            Result.success(account)
        } catch (e: ApiException) {
            val message = when (e.statusCode) {
                10 -> "Developer Error (10): Check SHA-1 or Web Client ID in Google Console"
                7 -> "Network Error: Check internet connection"
                12500 -> "Sign-In Failed (12500): Check if app is registered in Google Console"
                else -> "Sign-In Error (${e.statusCode})"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(Exception("Sign-In failed: ${e.message}"))
        }
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        signInClient.signOut()
    }

    fun getDriveService(account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            AndroidHttp.newCompatibleTransport(),
            GsonFactory(),
            credential
        ).setApplicationName("LifeMarker").build()
    }
}

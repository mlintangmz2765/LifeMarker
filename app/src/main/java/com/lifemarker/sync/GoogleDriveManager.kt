package com.lifemarker.sync

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val driveServiceFactory: GoogleDriveService
) {
    private val DB_NAME = "lifemarker.db"
    private val MIME_TYPE = "application/x-sqlite3"

    data class BackupInfo(val fileId: String, val modifiedTime: Long)


    suspend fun backupDatabase(account: GoogleSignInAccount): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveServiceFactory.getDriveService(account)
            val dbFile = context.getDatabasePath(DB_NAME)

            if (!dbFile.exists()) {
                return@withContext Result.failure(Exception("Database file not found"))
            }

            val fileId = getExistingBackupFileId(service)
            
            val fileMetadata = com.google.api.services.drive.model.File().apply {
                name = DB_NAME
                mimeType = MIME_TYPE
            }
            val mediaContent = FileContent(MIME_TYPE, dbFile)

            if (fileId != null) {
                service.files().update(fileId, fileMetadata, mediaContent).execute()
                Result.success("Backup updated successfully")
            } else {
                service.files().create(fileMetadata, mediaContent).execute()
                Result.success("Backup created successfully")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun restoreDatabase(account: GoogleSignInAccount): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveServiceFactory.getDriveService(account)
            val fileId = getExistingBackupFileId(service)
                ?: return@withContext Result.failure(Exception("No backup found on Google Drive"))

            val dbFile = context.getDatabasePath(DB_NAME)
            
            FileOutputStream(dbFile).use { outputStream ->
                service.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            }
            
            Result.success("Database restored successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBackupInfo(account: GoogleSignInAccount): BackupInfo? = withContext(Dispatchers.IO) {
        try {
            val service = driveServiceFactory.getDriveService(account)
            val result = service.files().list()
                .setQ("name='$DB_NAME' and trashed=false")
                .setSpaces("drive")
                .setFields("files(id, name, modifiedTime)")
                .execute()
            
            val file = result.files?.firstOrNull() ?: return@withContext null
            val time = file.modifiedTime?.value ?: 0L
            BackupInfo(file.id, time)
        } catch (e: Exception) {
            null
        }
    }

    private fun getExistingBackupFileId(service: Drive): String? {
        val result = service.files().list()
            .setQ("name='$DB_NAME' and trashed=false")
            .setSpaces("drive")
            .setFields("files(id, name)")
            .execute()
        return result.files?.firstOrNull()?.id
    }
}

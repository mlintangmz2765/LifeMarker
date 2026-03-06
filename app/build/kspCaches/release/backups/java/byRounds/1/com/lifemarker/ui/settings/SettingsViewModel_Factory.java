package com.lifemarker.ui.settings;

import com.lifemarker.sync.GoogleDriveManager;
import com.lifemarker.sync.GoogleDriveService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<GoogleDriveService> driveServiceProvider;

  private final Provider<GoogleDriveManager> driveManagerProvider;

  public SettingsViewModel_Factory(Provider<GoogleDriveService> driveServiceProvider,
      Provider<GoogleDriveManager> driveManagerProvider) {
    this.driveServiceProvider = driveServiceProvider;
    this.driveManagerProvider = driveManagerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(driveServiceProvider.get(), driveManagerProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<GoogleDriveService> driveServiceProvider,
      Provider<GoogleDriveManager> driveManagerProvider) {
    return new SettingsViewModel_Factory(driveServiceProvider, driveManagerProvider);
  }

  public static SettingsViewModel newInstance(GoogleDriveService driveService,
      GoogleDriveManager driveManager) {
    return new SettingsViewModel(driveService, driveManager);
  }
}

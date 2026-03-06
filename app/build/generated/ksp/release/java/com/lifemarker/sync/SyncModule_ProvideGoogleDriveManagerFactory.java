package com.lifemarker.sync;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SyncModule_ProvideGoogleDriveManagerFactory implements Factory<GoogleDriveManager> {
  private final Provider<Context> contextProvider;

  private final Provider<GoogleDriveService> googleDriveServiceProvider;

  public SyncModule_ProvideGoogleDriveManagerFactory(Provider<Context> contextProvider,
      Provider<GoogleDriveService> googleDriveServiceProvider) {
    this.contextProvider = contextProvider;
    this.googleDriveServiceProvider = googleDriveServiceProvider;
  }

  @Override
  public GoogleDriveManager get() {
    return provideGoogleDriveManager(contextProvider.get(), googleDriveServiceProvider.get());
  }

  public static SyncModule_ProvideGoogleDriveManagerFactory create(
      Provider<Context> contextProvider, Provider<GoogleDriveService> googleDriveServiceProvider) {
    return new SyncModule_ProvideGoogleDriveManagerFactory(contextProvider, googleDriveServiceProvider);
  }

  public static GoogleDriveManager provideGoogleDriveManager(Context context,
      GoogleDriveService googleDriveService) {
    return Preconditions.checkNotNullFromProvides(SyncModule.INSTANCE.provideGoogleDriveManager(context, googleDriveService));
  }
}

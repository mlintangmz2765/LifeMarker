package com.lifemarker.sync;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GoogleDriveManager_Factory implements Factory<GoogleDriveManager> {
  private final Provider<Context> contextProvider;

  private final Provider<GoogleDriveService> driveServiceFactoryProvider;

  public GoogleDriveManager_Factory(Provider<Context> contextProvider,
      Provider<GoogleDriveService> driveServiceFactoryProvider) {
    this.contextProvider = contextProvider;
    this.driveServiceFactoryProvider = driveServiceFactoryProvider;
  }

  @Override
  public GoogleDriveManager get() {
    return newInstance(contextProvider.get(), driveServiceFactoryProvider.get());
  }

  public static GoogleDriveManager_Factory create(Provider<Context> contextProvider,
      Provider<GoogleDriveService> driveServiceFactoryProvider) {
    return new GoogleDriveManager_Factory(contextProvider, driveServiceFactoryProvider);
  }

  public static GoogleDriveManager newInstance(Context context,
      GoogleDriveService driveServiceFactory) {
    return new GoogleDriveManager(context, driveServiceFactory);
  }
}

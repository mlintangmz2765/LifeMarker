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
public final class SyncModule_ProvideGoogleDriveServiceFactory implements Factory<GoogleDriveService> {
  private final Provider<Context> contextProvider;

  public SyncModule_ProvideGoogleDriveServiceFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public GoogleDriveService get() {
    return provideGoogleDriveService(contextProvider.get());
  }

  public static SyncModule_ProvideGoogleDriveServiceFactory create(
      Provider<Context> contextProvider) {
    return new SyncModule_ProvideGoogleDriveServiceFactory(contextProvider);
  }

  public static GoogleDriveService provideGoogleDriveService(Context context) {
    return Preconditions.checkNotNullFromProvides(SyncModule.INSTANCE.provideGoogleDriveService(context));
  }
}

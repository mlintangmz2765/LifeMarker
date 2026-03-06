package com.lifemarker.di;

import com.lifemarker.data.local.AppDatabase;
import com.lifemarker.data.local.dao.MarkerDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DatabaseModule_ProvideMarkerDaoFactory implements Factory<MarkerDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideMarkerDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MarkerDao get() {
    return provideMarkerDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideMarkerDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideMarkerDaoFactory(dbProvider);
  }

  public static MarkerDao provideMarkerDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMarkerDao(db));
  }
}

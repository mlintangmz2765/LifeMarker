package com.lifemarker.di;

import android.content.Context;
import com.lifemarker.data.local.AppDatabase;
import com.lifemarker.data.local.dao.CategoryDao;
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
public final class DatabaseModule_ProvideAppDatabaseFactory implements Factory<AppDatabase> {
  private final Provider<Context> contextProvider;

  private final Provider<CategoryDao> providerProvider;

  public DatabaseModule_ProvideAppDatabaseFactory(Provider<Context> contextProvider,
      Provider<CategoryDao> providerProvider) {
    this.contextProvider = contextProvider;
    this.providerProvider = providerProvider;
  }

  @Override
  public AppDatabase get() {
    return provideAppDatabase(contextProvider.get(), providerProvider);
  }

  public static DatabaseModule_ProvideAppDatabaseFactory create(Provider<Context> contextProvider,
      Provider<CategoryDao> providerProvider) {
    return new DatabaseModule_ProvideAppDatabaseFactory(contextProvider, providerProvider);
  }

  public static AppDatabase provideAppDatabase(Context context, Provider<CategoryDao> provider) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAppDatabase(context, provider));
  }
}

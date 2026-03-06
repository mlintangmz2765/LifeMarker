package com.lifemarker.data.repository;

import com.lifemarker.data.local.dao.CategoryDao;
import com.lifemarker.data.local.dao.MarkerDao;
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
public final class MarkerRepositoryImpl_Factory implements Factory<MarkerRepositoryImpl> {
  private final Provider<MarkerDao> markerDaoProvider;

  private final Provider<CategoryDao> categoryDaoProvider;

  public MarkerRepositoryImpl_Factory(Provider<MarkerDao> markerDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    this.markerDaoProvider = markerDaoProvider;
    this.categoryDaoProvider = categoryDaoProvider;
  }

  @Override
  public MarkerRepositoryImpl get() {
    return newInstance(markerDaoProvider.get(), categoryDaoProvider.get());
  }

  public static MarkerRepositoryImpl_Factory create(Provider<MarkerDao> markerDaoProvider,
      Provider<CategoryDao> categoryDaoProvider) {
    return new MarkerRepositoryImpl_Factory(markerDaoProvider, categoryDaoProvider);
  }

  public static MarkerRepositoryImpl newInstance(MarkerDao markerDao, CategoryDao categoryDao) {
    return new MarkerRepositoryImpl(markerDao, categoryDao);
  }
}

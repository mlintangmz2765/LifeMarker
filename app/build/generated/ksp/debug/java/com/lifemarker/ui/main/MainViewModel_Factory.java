package com.lifemarker.ui.main;

import com.lifemarker.domain.repository.CategoryRepository;
import com.lifemarker.domain.repository.MarkerRepository;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<MarkerRepository> markerRepositoryProvider;

  private final Provider<CategoryRepository> categoryRepositoryProvider;

  public MainViewModel_Factory(Provider<MarkerRepository> markerRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    this.markerRepositoryProvider = markerRepositoryProvider;
    this.categoryRepositoryProvider = categoryRepositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(markerRepositoryProvider.get(), categoryRepositoryProvider.get());
  }

  public static MainViewModel_Factory create(Provider<MarkerRepository> markerRepositoryProvider,
      Provider<CategoryRepository> categoryRepositoryProvider) {
    return new MainViewModel_Factory(markerRepositoryProvider, categoryRepositoryProvider);
  }

  public static MainViewModel newInstance(MarkerRepository markerRepository,
      CategoryRepository categoryRepository) {
    return new MainViewModel(markerRepository, categoryRepository);
  }
}

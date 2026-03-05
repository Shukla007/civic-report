package com.civicreport.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import com.civicreport.data.repository.ReportRepository;
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
    "cast",
    "deprecation"
})
public final class ReportDetailViewModel_Factory implements Factory<ReportDetailViewModel> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ReportDetailViewModel_Factory(Provider<ReportRepository> reportRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ReportDetailViewModel get() {
    return newInstance(reportRepositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static ReportDetailViewModel_Factory create(
      Provider<ReportRepository> reportRepositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ReportDetailViewModel_Factory(reportRepositoryProvider, savedStateHandleProvider);
  }

  public static ReportDetailViewModel newInstance(ReportRepository reportRepository,
      SavedStateHandle savedStateHandle) {
    return new ReportDetailViewModel(reportRepository, savedStateHandle);
  }
}

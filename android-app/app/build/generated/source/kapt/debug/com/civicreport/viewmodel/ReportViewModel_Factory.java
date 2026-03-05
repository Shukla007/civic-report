package com.civicreport.viewmodel;

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
public final class ReportViewModel_Factory implements Factory<ReportViewModel> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public ReportViewModel_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public ReportViewModel get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static ReportViewModel_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new ReportViewModel_Factory(reportRepositoryProvider);
  }

  public static ReportViewModel newInstance(ReportRepository reportRepository) {
    return new ReportViewModel(reportRepository);
  }
}

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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public DashboardViewModel_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<ReportRepository> reportRepositoryProvider) {
    return new DashboardViewModel_Factory(reportRepositoryProvider);
  }

  public static DashboardViewModel newInstance(ReportRepository reportRepository) {
    return new DashboardViewModel(reportRepository);
  }
}

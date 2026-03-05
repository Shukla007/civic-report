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
public final class TrackViewModel_Factory implements Factory<TrackViewModel> {
  private final Provider<ReportRepository> reportRepositoryProvider;

  public TrackViewModel_Factory(Provider<ReportRepository> reportRepositoryProvider) {
    this.reportRepositoryProvider = reportRepositoryProvider;
  }

  @Override
  public TrackViewModel get() {
    return newInstance(reportRepositoryProvider.get());
  }

  public static TrackViewModel_Factory create(Provider<ReportRepository> reportRepositoryProvider) {
    return new TrackViewModel_Factory(reportRepositoryProvider);
  }

  public static TrackViewModel newInstance(ReportRepository reportRepository) {
    return new TrackViewModel(reportRepository);
  }
}

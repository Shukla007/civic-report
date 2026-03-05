package com.civicreport.data.repository;

import android.content.Context;
import com.civicreport.data.api.CivicReportApi;
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
    "cast",
    "deprecation"
})
public final class ReportRepository_Factory implements Factory<ReportRepository> {
  private final Provider<CivicReportApi> apiProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<Context> contextProvider;

  public ReportRepository_Factory(Provider<CivicReportApi> apiProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Context> contextProvider) {
    this.apiProvider = apiProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ReportRepository get() {
    return newInstance(apiProvider.get(), authRepositoryProvider.get(), contextProvider.get());
  }

  public static ReportRepository_Factory create(Provider<CivicReportApi> apiProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Context> contextProvider) {
    return new ReportRepository_Factory(apiProvider, authRepositoryProvider, contextProvider);
  }

  public static ReportRepository newInstance(CivicReportApi api, AuthRepository authRepository,
      Context context) {
    return new ReportRepository(api, authRepository, context);
  }
}

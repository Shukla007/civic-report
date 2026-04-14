package com.civicreport.di;

import android.content.Context;
import com.civicreport.data.local.ReportDao;
import com.civicreport.data.repository.AuthRepository;
import com.civicreport.data.repository.ReportRepository;
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
    "cast",
    "deprecation"
})
public final class AppModule_ProvideReportRepositoryFactory implements Factory<ReportRepository> {
  private final Provider<ReportDao> reportDaoProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<Context> contextProvider;

  public AppModule_ProvideReportRepositoryFactory(Provider<ReportDao> reportDaoProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<Context> contextProvider) {
    this.reportDaoProvider = reportDaoProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ReportRepository get() {
    return provideReportRepository(reportDaoProvider.get(), authRepositoryProvider.get(), contextProvider.get());
  }

  public static AppModule_ProvideReportRepositoryFactory create(
      Provider<ReportDao> reportDaoProvider, Provider<AuthRepository> authRepositoryProvider,
      Provider<Context> contextProvider) {
    return new AppModule_ProvideReportRepositoryFactory(reportDaoProvider, authRepositoryProvider, contextProvider);
  }

  public static ReportRepository provideReportRepository(ReportDao reportDao,
      AuthRepository authRepository, Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideReportRepository(reportDao, authRepository, context));
  }
}

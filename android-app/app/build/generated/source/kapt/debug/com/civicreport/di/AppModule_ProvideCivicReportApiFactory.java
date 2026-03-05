package com.civicreport.di;

import com.civicreport.data.api.CivicReportApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
    "cast",
    "deprecation"
})
public final class AppModule_ProvideCivicReportApiFactory implements Factory<CivicReportApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideCivicReportApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public CivicReportApi get() {
    return provideCivicReportApi(retrofitProvider.get());
  }

  public static AppModule_ProvideCivicReportApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideCivicReportApiFactory(retrofitProvider);
  }

  public static CivicReportApi provideCivicReportApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCivicReportApi(retrofit));
  }
}

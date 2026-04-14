package com.civicreport.di

import android.content.Context
import androidx.room.Room
import com.civicreport.data.local.CivicReportDatabase
import com.civicreport.data.local.ReportDao
import com.civicreport.data.repository.AuthRepository
import com.civicreport.data.repository.ReportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CivicReportDatabase {
        return Room.databaseBuilder(
            context,
            CivicReportDatabase::class.java,
            "civic_report_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDao(database: CivicReportDatabase): ReportDao {
        return database.reportDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context
    ): AuthRepository {
        return AuthRepository(context)
    }

    @Provides
    @Singleton
    fun provideReportRepository(
        reportDao: ReportDao,
        authRepository: AuthRepository,
        @ApplicationContext context: Context
    ): ReportRepository {
        return ReportRepository(reportDao, authRepository, context)
    }
}

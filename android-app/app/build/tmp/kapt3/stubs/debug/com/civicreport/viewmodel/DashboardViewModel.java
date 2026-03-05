package com.civicreport.viewmodel;

import androidx.lifecycle.ViewModel;
import com.civicreport.data.model.Report;
import com.civicreport.data.repository.ApiResult;
import com.civicreport.data.repository.ReportRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ,\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0002J\u0006\u0010\u0015\u001a\u00020\rJ\u0006\u0010\u0016\u001a\u00020\rJ\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u0013R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0019"}, d2 = {"Lcom/civicreport/viewmodel/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "reportRepository", "Lcom/civicreport/data/repository/ReportRepository;", "(Lcom/civicreport/data/repository/ReportRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/civicreport/viewmodel/DashboardState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "filterReports", "", "Lcom/civicreport/data/model/Report;", "reports", "query", "", "status", "loadReports", "logout", "updateSearchQuery", "updateStatusFilter", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.repository.ReportRepository reportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.civicreport.viewmodel.DashboardState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.DashboardState> state = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.civicreport.data.repository.ReportRepository reportRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.DashboardState> getState() {
        return null;
    }
    
    public final void loadReports() {
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void updateStatusFilter(@org.jetbrains.annotations.NotNull()
    java.lang.String status) {
    }
    
    public final void logout() {
    }
    
    private final java.util.List<com.civicreport.data.model.Report> filterReports(java.util.List<com.civicreport.data.model.Report> reports, java.lang.String query, java.lang.String status) {
        return null;
    }
    
    public final void clearError() {
    }
}
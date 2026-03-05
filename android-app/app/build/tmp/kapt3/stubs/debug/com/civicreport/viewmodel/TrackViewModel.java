package com.civicreport.viewmodel;

import androidx.lifecycle.ViewModel;
import com.civicreport.data.model.Report;
import com.civicreport.data.repository.ApiResult;
import com.civicreport.data.repository.ReportRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\rJ\u0006\u0010\u000f\u001a\u00020\rJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0012R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0013"}, d2 = {"Lcom/civicreport/viewmodel/TrackViewModel;", "Landroidx/lifecycle/ViewModel;", "reportRepository", "Lcom/civicreport/data/repository/ReportRepository;", "(Lcom/civicreport/data/repository/ReportRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/civicreport/viewmodel/TrackState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "clearReport", "searchReport", "updateSearchQuery", "query", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class TrackViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.repository.ReportRepository reportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.civicreport.viewmodel.TrackState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.TrackState> state = null;
    
    @javax.inject.Inject()
    public TrackViewModel(@org.jetbrains.annotations.NotNull()
    com.civicreport.data.repository.ReportRepository reportRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.TrackState> getState() {
        return null;
    }
    
    public final void updateSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void searchReport() {
    }
    
    public final void clearError() {
    }
    
    public final void clearReport() {
    }
}
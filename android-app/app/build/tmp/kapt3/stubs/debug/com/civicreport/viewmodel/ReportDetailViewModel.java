package com.civicreport.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.civicreport.data.model.Report;
import com.civicreport.data.repository.ApiResult;
import com.civicreport.data.repository.ReportRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u0006\u0010\u0013\u001a\u00020\u0011J\u0006\u0010\u0014\u001a\u00020\u0011J\u0006\u0010\u0015\u001a\u00020\u0011J\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u000bJ\u0006\u0010\u0018\u001a\u00020\u0011J\u000e\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u000bR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001b"}, d2 = {"Lcom/civicreport/viewmodel/ReportDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "reportRepository", "Lcom/civicreport/data/repository/ReportRepository;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Lcom/civicreport/data/repository/ReportRepository;Landroidx/lifecycle/SavedStateHandle;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/civicreport/viewmodel/ReportDetailState;", "reportId", "", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearError", "", "clearUpdateSuccess", "hideUpdateDialog", "loadReport", "showUpdateDialog", "updateNotes", "notes", "updateReport", "updateSelectedStatus", "status", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReportDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.repository.ReportRepository reportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String reportId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.civicreport.viewmodel.ReportDetailState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.ReportDetailState> state = null;
    
    @javax.inject.Inject()
    public ReportDetailViewModel(@org.jetbrains.annotations.NotNull()
    com.civicreport.data.repository.ReportRepository reportRepository, @org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.ReportDetailState> getState() {
        return null;
    }
    
    public final void loadReport() {
    }
    
    public final void showUpdateDialog() {
    }
    
    public final void hideUpdateDialog() {
    }
    
    public final void updateSelectedStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String status) {
    }
    
    public final void updateNotes(@org.jetbrains.annotations.NotNull()
    java.lang.String notes) {
    }
    
    public final void updateReport() {
    }
    
    public final void clearError() {
    }
    
    public final void clearUpdateSuccess() {
    }
}
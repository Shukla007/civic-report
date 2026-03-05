package com.civicreport.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicreport.data.model.Report
import com.civicreport.data.repository.ApiResult
import com.civicreport.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportDetailState(
    val report: Report? = null,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null,
    val updateSuccess: Boolean = false,
    val showUpdateDialog: Boolean = false,
    val selectedStatus: String = "",
    val updateNotes: String = ""
)

@HiltViewModel
class ReportDetailViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val reportId: String = savedStateHandle.get<String>("reportId") ?: ""
    
    private val _state = MutableStateFlow(ReportDetailState())
    val state: StateFlow<ReportDetailState> = _state.asStateFlow()
    
    init {
        loadReport()
    }
    
    fun loadReport() {
        if (reportId.isBlank()) return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            when (val result = reportRepository.getReportById(reportId)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        report = result.data,
                        selectedStatus = result.data.status
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is ApiResult.Loading -> {}
            }
        }
    }
    
    fun showUpdateDialog() {
        _state.value = _state.value.copy(
            showUpdateDialog = true,
            selectedStatus = _state.value.report?.status ?: "pending",
            updateNotes = ""
        )
    }
    
    fun hideUpdateDialog() {
        _state.value = _state.value.copy(showUpdateDialog = false)
    }
    
    fun updateSelectedStatus(status: String) {
        _state.value = _state.value.copy(selectedStatus = status)
    }
    
    fun updateNotes(notes: String) {
        _state.value = _state.value.copy(updateNotes = notes)
    }
    
    fun updateReport() {
        val state = _state.value
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, error = null)
            
            when (val result = reportRepository.updateReport(
                reportId = reportId,
                status = state.selectedStatus,
                notes = state.updateNotes.takeIf { it.isNotBlank() }
            )) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        report = result.data,
                        updateSuccess = true,
                        showUpdateDialog = false
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isUpdating = false,
                        error = result.message
                    )
                }
                is ApiResult.Loading -> {}
            }
        }
    }
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    fun clearUpdateSuccess() {
        _state.value = _state.value.copy(updateSuccess = false)
    }
}

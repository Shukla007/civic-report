package com.civicreport.viewmodel

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

data class DashboardState(
    val reports: List<Report> = emptyList(),
    val filteredReports: List<Report> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: String = "all",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    
    init {
        loadReports()
    }
    
    fun loadReports() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            when (val result = reportRepository.getAllReports()) {
                is ApiResult.Success -> {
                    val reports = result.data.sortedByDescending { it.createdAt }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        reports = reports,
                        filteredReports = filterReports(reports, _state.value.searchQuery, _state.value.statusFilter)
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
    
    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(
            searchQuery = query,
            filteredReports = filterReports(_state.value.reports, query, _state.value.statusFilter)
        )
    }
    
    fun updateStatusFilter(status: String) {
        _state.value = _state.value.copy(
            statusFilter = status,
            filteredReports = filterReports(_state.value.reports, _state.value.searchQuery, status)
        )
    }
    
    fun logout() {
        viewModelScope.launch {
            reportRepository.logout()
        }
    }
    
    private fun filterReports(reports: List<Report>, query: String, status: String): List<Report> {
        return reports.filter { report ->
            val matchesQuery = query.isBlank() || 
                report.reportId.contains(query, ignoreCase = true) ||
                report.title.contains(query, ignoreCase = true)
            
            val matchesStatus = status == "all" || report.status == status
            
            matchesQuery && matchesStatus
        }
    }
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

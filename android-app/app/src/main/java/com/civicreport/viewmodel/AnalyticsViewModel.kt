package com.civicreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civicreport.data.model.AnalyticsData
import com.civicreport.data.model.Report
import com.civicreport.data.repository.ApiResult
import com.civicreport.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsState(
    val analytics: AnalyticsData = AnalyticsData(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()
    
    init {
        loadAnalytics()
    }
    
    fun loadAnalytics() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            when (val result = reportRepository.getAllReports()) {
                is ApiResult.Success -> {
                    val reports = result.data
                    val analytics = calculateAnalytics(reports)
                    _state.value = _state.value.copy(
                        isLoading = false,
                        analytics = analytics
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
    
    private fun calculateAnalytics(reports: List<Report>): AnalyticsData {
        val total = reports.size
        val pending = reports.count { it.status == "pending" }
        val acknowledged = reports.count { it.status == "acknowledged" }
        val inProgress = reports.count { it.status == "in-progress" }
        val resolved = reports.count { it.status == "resolved" }
        
        val byCategory = reports.groupBy { it.category }
            .mapValues { it.value.size }
        
        val byPriority = reports.groupBy { it.priority }
            .mapValues { it.value.size }
        
        return AnalyticsData(
            total = total,
            pending = pending,
            acknowledged = acknowledged,
            inProgress = inProgress,
            resolved = resolved,
            byCategory = byCategory,
            byPriority = byPriority
        )
    }
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

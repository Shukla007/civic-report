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

data class TrackState(
    val searchQuery: String = "",
    val report: Report? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(TrackState())
    val state: StateFlow<TrackState> = _state.asStateFlow()
    
    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query.uppercase())
    }
    
    fun searchReport() {
        val query = _state.value.searchQuery.trim()
        if (query.isBlank()) {
            _state.value = _state.value.copy(error = "Please enter a Report ID")
            return
        }
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, report = null)
            
            when (val result = reportRepository.getReportById(query)) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        report = result.data
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
    
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
    
    fun clearReport() {
        _state.value = TrackState()
    }
}

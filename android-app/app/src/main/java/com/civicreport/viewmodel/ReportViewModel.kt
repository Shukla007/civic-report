package com.civicreport.viewmodel

import android.net.Uri
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

data class ReportFormState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: String = "medium",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photoUris: List<Uri> = emptyList(),
    val voiceNoteUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val createdReportId: String? = null
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    
    private val _formState = MutableStateFlow(ReportFormState())
    val formState: StateFlow<ReportFormState> = _formState.asStateFlow()
    
    fun updateTitle(title: String) {
        _formState.value = _formState.value.copy(title = title)
    }
    
    fun updateDescription(description: String) {
        _formState.value = _formState.value.copy(description = description)
    }
    
    fun updateCategory(category: String) {
        _formState.value = _formState.value.copy(category = category)
    }
    
    fun updatePriority(priority: String) {
        _formState.value = _formState.value.copy(priority = priority)
    }
    
    fun updateLocation(latitude: Double, longitude: Double) {
        _formState.value = _formState.value.copy(latitude = latitude, longitude = longitude)
    }
    
    fun addPhoto(uri: Uri) {
        val currentPhotos = _formState.value.photoUris.toMutableList()
        if (currentPhotos.size < 5) {
            currentPhotos.add(uri)
            _formState.value = _formState.value.copy(photoUris = currentPhotos)
        }
    }
    
    fun removePhoto(uri: Uri) {
        val currentPhotos = _formState.value.photoUris.toMutableList()
        currentPhotos.remove(uri)
        _formState.value = _formState.value.copy(photoUris = currentPhotos)
    }
    
    fun setVoiceNote(uri: Uri?) {
        _formState.value = _formState.value.copy(voiceNoteUri = uri)
    }
    
    fun clearError() {
        _formState.value = _formState.value.copy(error = null)
    }
    
    fun submitReport() {
        val state = _formState.value
        
        if (state.title.isBlank()) {
            _formState.value = state.copy(error = "Title is required")
            return
        }
        
        if (state.category.isBlank()) {
            _formState.value = state.copy(error = "Category is required")
            return
        }
        
        if (state.latitude == null || state.longitude == null) {
            _formState.value = state.copy(error = "Location is required")
            return
        }
        
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isLoading = true, error = null)
            
            when (val result = reportRepository.createReport(
                title = state.title,
                description = state.description.ifBlank { "No description provided" },
                category = state.category,
                priority = state.priority,
                latitude = state.latitude,
                longitude = state.longitude,
                photoUris = state.photoUris,
                voiceNoteUri = state.voiceNoteUri
            )) {
                is ApiResult.Success -> {
                    _formState.value = _formState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        createdReportId = result.data.reportId
                    )
                }
                is ApiResult.Error -> {
                    _formState.value = _formState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is ApiResult.Loading -> {}
            }
        }
    }
    
    fun resetForm() {
        _formState.value = ReportFormState()
    }
}

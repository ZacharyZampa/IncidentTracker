package com.zacharyzampa.incidenttracker.views

import androidx.lifecycle.*
import com.zacharyzampa.incidenttracker.dao.IncidentRepository
import com.zacharyzampa.incidenttracker.entity.Incident
import kotlinx.coroutines.launch

class IncidentViewModel(private val repository: IncidentRepository) : ViewModel() {

    // Using LiveData and caching what incidents returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allIncidents: LiveData<List<Incident>> = repository.incidents.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(incident: Incident) = viewModelScope.launch {
        repository.insert(incident)
    }

    fun delete(incident: Incident) = viewModelScope.launch {
        repository.delete(incident)
    }
}

class IncidentViewModelFactory(private val repository: IncidentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncidentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncidentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
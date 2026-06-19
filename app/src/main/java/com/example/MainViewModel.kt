package com.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DoramaRepository = DoramaRepository()) : ViewModel() {

    private val _doramas = MutableStateFlow<List<Dorama>>(emptyList())
    val doramas: StateFlow<List<Dorama>> = _doramas.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Mock Login State
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    init {
        loadDoramas()
        viewModelScope.launch {
            repository.doramasFlow.collect {
                _doramas.value = it
            }
        }
    }

    fun loadDoramas() {
        viewModelScope.launch {
            _isLoading.value = true
            _doramas.value = repository.fetchDoramas()
            _isLoading.value = false
        }
    }

    fun login(username: String) {
        _isLoggedIn.value = true
        _isAdmin.value = username.lowercase() == "admin"
    }

    fun logout() {
        _isLoggedIn.value = false
        _isAdmin.value = false
    }

    fun addDorama(title: String, imageUrl: String, description: String, videoUrl: String) {
        viewModelScope.launch {
            repository.addDorama(title, imageUrl, description, videoUrl)
        }
    }

    fun deleteDorama(id: String) {
        viewModelScope.launch {
            repository.deleteDorama(id)
        }
    }
}

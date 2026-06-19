package com.example

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoramaRepository {
    private val _mockDoramas = mutableListOf(
        Dorama(
            id = "1",
            title = "Crash Landing on You",
            imageUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400&q=80",
            description = "A South Korean heiress paraglides into North Korea and into the life of a military officer.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        ),
        Dorama(
            id = "2",
            title = "Itaewon Class",
            imageUrl = "https://images.unsplash.com/photo-1542289650-25255474ca8f?w=400&q=80",
            description = "An ex-con opens a street bar in Itaewon, while also seeking revenge.",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
        )
    )

    private val _doramasFlow = MutableStateFlow<List<Dorama>>(_mockDoramas.toList())
    val doramasFlow: StateFlow<List<Dorama>> = _doramasFlow.asStateFlow()

    suspend fun fetchDoramas(): List<Dorama> {
        return try {
            val response = ApiClient.apiService.getDoramas()
            response.doramas
        } catch (e: Exception) {
            // Fallback to mock data if the API is not actually available
            _doramasFlow.value
        }
    }

    suspend fun getDorama(id: String): Dorama? {
        return try {
            ApiClient.apiService.getDorama(id)
        } catch (e: Exception) {
            _doramasFlow.value.find { it.id == id }
        }
    }

    suspend fun addDorama(title: String, imageUrl: String, description: String, videoUrl: String) {
        val newDorama = Dorama(
            id = (_doramasFlow.value.size + 1).toString(),
            title = title,
            imageUrl = imageUrl,
            description = description,
            videoUrl = videoUrl
        )
        try {
            ApiClient.apiService.addDorama(newDorama)
        } catch (e: Exception) {
            // Mock adding
            _mockDoramas.add(newDorama)
            _doramasFlow.value = _mockDoramas.toList()
        }
    }

    suspend fun deleteDorama(id: String) {
        try {
            ApiClient.apiService.deleteDorama(id)
        } catch (e: Exception) {
            // Mock deleting
            _mockDoramas.removeAll { it.id == id }
            _doramasFlow.value = _mockDoramas.toList()
        }
    }
}

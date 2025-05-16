package com.rubylearner.ktorclient.services

import com.rubylearner.ktorclient.models.PriceModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val apiService: ApiService) {

    fun fetchCryptoPrice(page: Int): Flow<PriceUiState> {
        return flow {
            emit(PriceUiState.Loading)
            try {
                val response: List<PriceModels> = apiService.getPriceApi(page = page)
                emit(PriceUiState.Success(response))
            } catch (e: Exception) {
                emit(PriceUiState.Error(e.toString()))
            }
        }
    }

}

sealed class PriceUiState {
    data class Success(val priceList: List<PriceModels>) : PriceUiState()
    data class Error(val errorMessage: String) : PriceUiState()
    data object Loading : PriceUiState()
}
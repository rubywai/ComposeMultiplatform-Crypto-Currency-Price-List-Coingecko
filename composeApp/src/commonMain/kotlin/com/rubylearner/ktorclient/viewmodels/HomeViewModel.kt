package com.rubylearner.ktorclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubylearner.ktorclient.services.PriceUiState
import com.rubylearner.ktorclient.services.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(),KoinComponent {

   private val repository: Repository by inject()



    private val _cryptoStateList = MutableStateFlow<PriceUiState>(PriceUiState.Loading)
    val cryptoStateList : StateFlow<PriceUiState> = _cryptoStateList

    fun fetchPriceList(page : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchCryptoPrice(page).collect{
                _cryptoStateList.value = it
            }
        }
    }

}
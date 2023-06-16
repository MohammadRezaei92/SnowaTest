package com.snowa.snowaproject.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowa.snowaproject.ViewState
import com.snowa.snowaproject.data.Coin
import com.snowa.snowaproject.data.Repository
import com.snowa.snowaproject.getViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val coinList = MutableStateFlow<ViewState<List<Coin>>>(ViewState.Loading)

    fun getCoinList() = viewModelScope.launch {
        coinList.emit(ViewState.Loading)
        coinList.emit(
            getViewState {
                repository.getCoins()
            }
        )
    }
}
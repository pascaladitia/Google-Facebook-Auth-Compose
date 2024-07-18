package com.pascal.dansandroid.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.base.UiState
import com.pascal.dansandroid.domain.model.ParamData
import com.pascal.dansandroid.domain.usecase.RemoteUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DataViewModel(
    private val remoteUC: RemoteUC
) : ViewModel() {

    private val _searchState = MutableStateFlow<UiState<List<ResponseDataItem>>>(UiState.Empty)
    val searchState: StateFlow<UiState<List<ResponseDataItem>>> = _searchState

    private val _listData = MutableStateFlow(PagingData.empty<ResponseDataItem>())
    val listData: StateFlow<PagingData<ResponseDataItem>> = _listData

    private val _detailState = MutableStateFlow<UiState<ResponseDataItem>>(UiState.Empty)
    val detailState: StateFlow<UiState<ResponseDataItem>> = _detailState

    suspend fun loadSearch(data: ParamData) {
        _searchState.value = UiState.Loading

        viewModelScope.launch {
            remoteUC.search(data).collect {
                try {
                    if (it.body()?.isEmpty() == true) {
                        _searchState.value = UiState.Error("Job Not Found")
                    } else {
                        _searchState.value = UiState.Success(it.body() ?: emptyList())
                    }
                } catch (e: Exception) {
                    _searchState.value = UiState.Error(e.message.toString())
                }
            }
        }
    }

    suspend fun loadListData() {
        remoteUC.listData()
            .cachedIn(viewModelScope)
            .collect {
                _listData.value = it
            }
    }

    suspend fun loadDetail(id: String) {
        _detailState.value = UiState.Loading

        viewModelScope.launch {
            remoteUC.detail(id).collect {
                try {
                    _detailState.value = UiState.Success(it.body() ?: ResponseDataItem())
                } catch (e: Exception) {
                    _detailState.value = UiState.Error(e.message.toString())
                }
            }
        }
    }
}
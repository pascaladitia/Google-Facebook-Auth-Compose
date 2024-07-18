package com.pascal.dansandroid.domain.usecase

import androidx.paging.PagingData
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.model.ParamData
import com.pascal.dansandroid.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteUC (private val repository: IRepository) {
    suspend fun search(data: ParamData): Flow<Response<List<ResponseDataItem>>> {
        return repository.search(data)
    }

    suspend fun listData(): Flow<PagingData<ResponseDataItem>> {
        return repository.listData()
    }

    suspend fun detail(id: String): Flow<Response<ResponseDataItem>> {
        return repository.detail(id)
    }
}
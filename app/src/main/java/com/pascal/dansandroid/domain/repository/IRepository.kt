package com.pascal.dansandroid.domain.repository

import androidx.paging.PagingData
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.model.ParamData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRepository {

    suspend fun search(data: ParamData): Flow<Response<List<ResponseDataItem>>>

    suspend fun listData(): Flow<PagingData<ResponseDataItem>>

    suspend fun detail(id: String): Flow<Response<ResponseDataItem>>

}
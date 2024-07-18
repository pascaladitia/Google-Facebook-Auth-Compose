package com.pascal.dansandroid.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pascal.dansandroid.data.remote.AppService
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem
import com.pascal.dansandroid.domain.model.ParamData
import com.pascal.dansandroid.domain.pagination.DataPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

internal class Repository(
    private val appService: AppService
) : IRepository {

    override suspend fun search(data: ParamData): Flow<Response<List<ResponseDataItem>>> {
        return flowOf(appService.search(data.description, data.location, data.full_time))
    }

    override suspend fun listData(): Flow<PagingData<ResponseDataItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                DataPagingSource(appService)
            }
        ).flow
    }

    override suspend fun detail(id: String): Flow<Response<ResponseDataItem>> {
        return flowOf(appService.detail(id))
    }
}
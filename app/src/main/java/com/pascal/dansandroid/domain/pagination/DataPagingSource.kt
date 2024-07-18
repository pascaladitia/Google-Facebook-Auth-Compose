package com.pascal.dansandroid.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pascal.dansandroid.data.remote.AppService
import com.pascal.dansandroid.data.remote.dtos.ResponseDataItem

class DataPagingSource(
    private val appService: AppService
) : PagingSource<Int, ResponseDataItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResponseDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponseDataItem> {
        return try {
            val page = params.key ?: 1
            val dataList = appService.listData(page.toString()).body()

            LoadResult.Page(
                data = dataList ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (dataList?.isEmpty() == true) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
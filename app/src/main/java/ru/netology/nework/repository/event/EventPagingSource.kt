package ru.netology.nework.repository.event

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.nework.api.EventsApi
import ru.netology.nework.model.Event
import java.io.IOException

class EventPagingSource(
    private val api: EventsApi
) : PagingSource<Long, Event>() {
    override fun getRefreshKey(state: PagingState<Long, Event>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Event> {
        try {
            val result = when (params) {
                is LoadParams.Append -> {
                    api.getEventsBefore(id = params.key, count = params.loadSize)

                }

                is LoadParams.Prepend -> {
                    return LoadResult.Page(
                        data = emptyList(), nextKey = null, prevKey = params.key
                    )
                }

                is LoadParams.Refresh -> {
                    api.getLatestEvents(params.loadSize)
                }
            }
            if (!result.isSuccessful) {
                throw HttpException(result)
            }
            val data = result.body().orEmpty()
            return LoadResult.Page(data, prevKey = params.key, nextKey = data.lastOrNull()?.id)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}
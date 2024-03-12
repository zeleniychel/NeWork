package ru.netology.nework.repository.mywall

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.nework.api.MyWallApi
import ru.netology.nework.model.Post
import java.io.IOException

class MyWallPagingSource(
    private val api: MyWallApi,
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Append -> {
                    api.getMyWallPostsBefore(id = params.key,  count = params.loadSize)

                }

                is LoadParams.Prepend -> {
                    return LoadResult.Page(
                        data = emptyList(), nextKey = null, prevKey = params.key
                    )
                }

                is LoadParams.Refresh -> {
                    api.getLatestMyWallPosts(params.loadSize)
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
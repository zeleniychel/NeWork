package ru.netology.nework.repository.wall

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import ru.netology.nework.api.WallApi
import ru.netology.nework.model.Post
import java.io.IOException

class WallPagingSource(
    private val api: WallApi,
    private val authorId: Long
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        try {
            val result = when (params) {
                is LoadParams.Append -> {
                    api.getWallPostsBefore(id = params.key, authorId = authorId, count = params.loadSize)

                }

                is LoadParams.Prepend -> {
                    return LoadResult.Page(
                        data = emptyList(), nextKey = null, prevKey = params.key
                    )
                }

                is LoadParams.Refresh -> {
                    api.getLatestWallPosts(params.loadSize)
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
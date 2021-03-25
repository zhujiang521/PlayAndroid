package com.zj.play.compose.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zj.model.room.entity.Article
import com.zj.network.base.PlayAndroidNetwork
import com.zj.network.service.ArticleService
import com.zj.network.service.OfficialService
import com.zj.play.compose.model.QueryArticle
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import java.io.IOException

class ArticlePagingSource(
    private val service: ArticleService,
    private val cid: Int
) : PagingSource<Int, Article>() {


    companion object {
        private const val TAG = "ArticlePagingSource"
        private const val GITHUB_STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            val response = service.getProject(position, cid)
            val repos = response.data.datas
            val prevKey = if (position > 1) position - 1 else null
            val nextKey = if (repos.isNotEmpty()) position + 1 else null
            LoadResult.Page(data = repos, prevKey = prevKey, nextKey = nextKey)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
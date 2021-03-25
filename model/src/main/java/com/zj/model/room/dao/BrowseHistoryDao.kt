package com.zj.model.room.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.zj.model.room.entity.Article


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Dao
interface BrowseHistoryDao {

    @Query("SELECT * FROM browse_history")
    suspend fun getAllArticle(): List<Article>

    @Query("SELECT * FROM browse_history where id = :id and local_type = :type")
    suspend fun getArticle(id: Int, type: Int): Article?

    @Query("SELECT * FROM browse_history where local_type = :type order by uid desc limit :page,20")
    suspend fun getHistoryArticleList(page: Int, type: Int): List<Article>

    @Query("SELECT * FROM browse_history where local_type = :type")
    suspend fun getArticleList(type: Int): List<Article>

    @Query("SELECT * FROM browse_history where local_type = :type")
    suspend fun getTopArticleList(type: Int): List<Article>

    @Query("SELECT * FROM browse_history where local_type = :type and chapter_id = :chapterId")
    suspend fun getArticleListForChapterId(type: Int, chapterId: Int): List<Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article): Int

    @Delete
    suspend fun delete(article: Article): Int

    @Delete
    suspend fun deleteList(articleList: List<Article>): Int

    @Query("DELETE FROM browse_history")
    suspend fun clearRepos()

    @Query("DELETE FROM browse_history where local_type = :type")
    suspend fun deleteAll(type: Int)

    @Query("DELETE FROM browse_history where local_type = :type and  chapter_id = :chapterId")
    suspend fun deleteAll(type: Int, chapterId: Int)

    @Query("SELECT * FROM browse_history where chapter_id = :cid")
    fun articleByCid(cid: Int): PagingSource<Int, Article>

}

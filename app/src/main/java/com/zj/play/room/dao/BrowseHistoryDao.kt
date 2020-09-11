package com.zj.play.room.dao

import androidx.room.*
import com.zj.play.room.entity.Article

/**
 * 版权：联想 版权所有
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

    @Query("SELECT * FROM browse_history order by uid limit :page*10,20")
    suspend fun getArticleList(page: Int): List<Article>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(articleList: List<Article>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article): Int

    @Delete
    suspend fun deleteList(articleList: List<Article>): Int

    @Query("DELETE FROM browse_history")
    suspend fun deleteAll()
}

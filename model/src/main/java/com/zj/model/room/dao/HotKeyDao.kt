package com.zj.model.room.dao

import androidx.room.*
import com.zj.model.room.entity.HotKey


/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Dao
interface HotKeyDao {

    @Query("SELECT * FROM hot_key order by uid desc")
    suspend fun getHotKeyList(): List<HotKey>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(HotKeyList: List<HotKey>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(HotKey: HotKey)

    @Update
    suspend fun update(HotKey: HotKey): Int

    @Delete
    suspend fun delete(HotKey: HotKey): Int

    @Delete
    suspend fun deleteList(HotKeyList: List<HotKey>): Int

    @Query("DELETE FROM hot_key")
    suspend fun deleteAll()
}

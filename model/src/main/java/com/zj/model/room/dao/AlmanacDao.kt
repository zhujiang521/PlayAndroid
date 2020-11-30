package com.zj.model.room.dao

import androidx.room.*
import com.zj.model.room.entity.Almanac


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Dao
interface AlmanacDao {

    @Query("SELECT * FROM almanac order by uid desc")
    suspend fun getAlmanacList(): List<Almanac>

    @Query("SELECT * FROM almanac where julian_day = :julianDay")
    suspend fun getAlmanac(julianDay: Int): Almanac?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(almanacList: List<Almanac>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(almanac: Almanac)

    @Update
    suspend fun update(almanac: Almanac): Int

    @Delete
    suspend fun delete(almanac: Almanac): Int

    @Delete
    suspend fun deleteList(almanacList: List<Almanac>): Int

    @Query("DELETE FROM almanac")
    suspend fun deleteAll()
}

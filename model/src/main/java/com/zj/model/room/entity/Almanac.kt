package com.zj.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/30/20
 * 描述：PlayAndroid
 *
 */
@Entity(tableName = "almanac")
data class Almanac(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "julian_day") val julianDay: Int = 0,
    @ColumnInfo(name = "img_uri") val imgUri: String,
)
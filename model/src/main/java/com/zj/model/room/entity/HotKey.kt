package com.zj.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/13
 * 描述：PlayAndroid
 *
 */
@Entity(tableName = "hot_key")
data class HotKey(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "link") val link: String = "",
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order") val order: Int = 0,
    @ColumnInfo(name = "visible") val visible: Int = 0
)
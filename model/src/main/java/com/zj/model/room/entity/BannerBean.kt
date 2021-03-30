package com.zj.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zj.banner.model.BaseBannerBean

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/16
 * 描述：PlayAndroid
 *
 */

@Entity(tableName = "banner_bean")
data class BannerBean(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "imagePath") val imagePath: String,
    @ColumnInfo(name = "isVisible") val isVisible: Int,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "file_path") var filePath: String = "",
    @ColumnInfo(name = "data") override var data: String?
) : BaseBannerBean()
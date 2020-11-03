package com.zj.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/9
 * 描述：PlayAndroid
 *
 */
@Entity(tableName = "project_classify")
data class ProjectClassify(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    //@ColumnInfo(name = "children") val children: List<Any> = arrayListOf(),
    @ColumnInfo(name = "course_id") val courseId: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order_classify") val order: Int,
    @ColumnInfo(name = "parent_chapter_id") val parentChapterId: Int,
    @ColumnInfo(name = "user_control_set_top") val userControlSetTop: Boolean,
    @ColumnInfo(name = "visible") val visible: Int
)
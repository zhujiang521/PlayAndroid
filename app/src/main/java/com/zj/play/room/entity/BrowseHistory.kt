package com.zj.play.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/11
 * 描述：PlayAndroid
 *
 */
@Entity(tableName = "browse_history")
data class Article(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "apk_link") val apkLink: String,
    @ColumnInfo(name = "audit") val audit: Int,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "can_edit") val canEdit: Boolean,
    @ColumnInfo(name = "chapter_id") val chapterId: Int,
    @ColumnInfo(name = "chapter_name") val chapterName: String,
    @ColumnInfo(name = "collect") var collect: Boolean,
    @ColumnInfo(name = "course_id") val courseId: Int,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "desc_md") val descMd: String,
    @ColumnInfo(name = "envelope_pic") val envelopePic: String,
    @ColumnInfo(name = "fresh") val fresh: Boolean,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "nice_date") val niceDate: String,
    @ColumnInfo(name = "nice_share_date") val niceShareDate: String,
    @ColumnInfo(name = "origin") val origin: String,
    @ColumnInfo(name = "prefix") val prefix: String,
    @ColumnInfo(name = "project_link") val projectLink: String,
    @ColumnInfo(name = "publish_time") val publishTime: Long,
    @ColumnInfo(name = "self_visible") val selfVisible: Int,
    @ColumnInfo(name = "share_date") val shareDate: Long,
    @ColumnInfo(name = "share_user") val shareUser: String,
    @ColumnInfo(name = "super_chapter_id") val superChapterId: Int,
    @ColumnInfo(name = "super_chapter_name") val superChapterName: String,
    //@ColumnInfo(name = "tags") val tags: List<Tag>,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "visible") val visible: Int,
    @ColumnInfo(name = "zan") val zan: Int
)

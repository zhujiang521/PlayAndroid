package com.zj.model.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiangz
 * 创建日期：2020/9/11
 * 描述：PlayAndroid
 *
 */

@Entity(tableName = "browse_history")
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "apk_link") val apkLink: String = "",
    @ColumnInfo(name = "audit") val audit: Int = 0,
    @ColumnInfo(name = "author") val author: String = "",
    @ColumnInfo(name = "can_edit") val canEdit: Boolean = true,
    @ColumnInfo(name = "chapter_id") val chapterId: Int = 0,
    @ColumnInfo(name = "chapter_name") val chapterName: String = "",
    @ColumnInfo(name = "collect") var collect: Boolean = false,
    @ColumnInfo(name = "course_id") val courseId: Int = 0,
    @ColumnInfo(name = "desc") var desc: String = "",
    @ColumnInfo(name = "desc_md") val descMd: String = "",
    @ColumnInfo(name = "envelope_pic") val envelopePic: String = "",
    @ColumnInfo(name = "fresh") val fresh: Boolean = false,
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "link") val link: String = "",
    @ColumnInfo(name = "nice_date") val niceDate: String = "",
    @ColumnInfo(name = "nice_share_date") val niceShareDate: String = "",
    @ColumnInfo(name = "origin") val origin: String = "",
    @ColumnInfo(name = "prefix") val prefix: String = "",
    @ColumnInfo(name = "project_link") val projectLink: String = "",
    @ColumnInfo(name = "publish_time") val publishTime: Long = 0L,
    @ColumnInfo(name = "self_visible") val selfVisible: Int = 0,
    @ColumnInfo(name = "share_date") val shareDate: Long = 0L,
    @ColumnInfo(name = "share_user") val shareUser: String = "",
    @ColumnInfo(name = "super_chapter_id") val superChapterId: Int = 0,
    @ColumnInfo(name = "super_chapter_name") val superChapterName: String = "",
    //@ColumnInfo(name = "tags") val tags: List<Tag>,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "type") val type: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int = 0,
    @ColumnInfo(name = "visible") val visible: Int = 0,
    @ColumnInfo(name = "zan") val zan: Int = 0,
    @ColumnInfo(name = "local_type") var localType: Int = 0
) : Parcelable

// 历史记录 localType
const val HISTORY = 10

// 首页置顶 localType
const val HOME_TOP = 20

// 首页 localType
const val HOME = 30

// 项目 localType
const val PROJECT = 40

// 公众号 localType
const val OFFICIAL = 50

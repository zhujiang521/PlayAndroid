package com.zj.play.logic.model


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiangz
 * 创建日期：2021/5/15
 * 描述：PlayAndroid
 *
 */

data class ArticleModel(
    val uid: Int = 0,
    val apkLink: String = "",
    val audit: Int = 0,
    val author: String = "",
    val canEdit: Boolean = true,
    val chapterId: Int = 0,
    val chapterName: String = "",
    var collect: Boolean = false,
    val courseId: Int = 0,
    var desc: String = "",
    val descMd: String = "",
    val envelopePic: String = "",
    val fresh: Boolean = false,
    val id: Int = 0,
    val link: String = "",
    val niceDate: String = "",
    val niceShareDate: String = "",
    val origin: String = "",
    val prefix: String = "",
    val projectLink: String = "",
    val publishTime: Long = 0L,
    val selfVisible: Int = 0,
    val shareDate: Long = 0L,
    val shareUser: String = "",
    val superChapterId: Int = 0,
    val superChapterName: String = "",
    var title: String = "",
    val type: Int = 0,
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0,
    var localType: Int = 0
)

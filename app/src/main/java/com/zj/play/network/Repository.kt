package com.zj.play.network

import android.util.Log
import androidx.lifecycle.liveData
import com.zj.play.model.Article
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * 版权：渤海新能 版权所有
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/17
 * 描述：PlayAndroid
 *
 */
object Repository {

    /**
     * 获banner
     */
    fun getBanner() = fire() {
        val bannerResponse = PlayAndroidNetwork.getBanner()
        if (bannerResponse.errorCode == 0) {
            val bannerList = bannerResponse.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
        }
    }


    fun getArticleList(page: Int) = fire {
        coroutineScope {
            val articleListDeferred = async { PlayAndroidNetwork.getArticleList(page - 1) }
            val articleList = articleListDeferred.await()
            if (page == 1) {
                val topArticleListDeferred = async { PlayAndroidNetwork.getTopArticleList() }
                val topArticleList = topArticleListDeferred.await()
                if (topArticleList.errorCode == 0 && articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(topArticleList.data)
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${topArticleList.errorCode}" + "  msg is ${topArticleList.errorMsg}"
                        )
                    )
                }
            } else {
                if (articleList.errorCode == 0) {
                    val res = arrayListOf<Article>()
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                        )
                    )
                }
            }
        }
    }

    /**
     * 获取搜索热词
     */
    fun getHotKey() = fire {
        val projectTree = PlayAndroidNetwork.getHotKey()
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    /**
     * 获取搜索结果
     */
    fun getQueryArticleList(page: Int, k: String) = fire {
        val projectTree = PlayAndroidNetwork.getQueryArticleList(page, k)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getProject(page: Int, cid: Int) = fire {
        val projectTree = PlayAndroidNetwork.getProject(page, cid)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList.datas)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getWxArticle(page: Int, cid: Int) = fire {
        val projectTree = PlayAndroidNetwork.getWxArticle(page, cid)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList.datas)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getLogin(username: String, password: String) = fire {
        val projectTree = PlayAndroidNetwork.getLogin(username, password)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getRegister(username: String, password: String, repassword: String) = fire {
        val projectTree = PlayAndroidNetwork.getRegister(username, password, repassword)
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

    fun getLogout() = fire {
        val projectTree = PlayAndroidNetwork.getLogout()
        if (projectTree.errorCode == 0) {
            val bannerList = projectTree.data
            Result.success(bannerList)
        } else {
            Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
        }
    }

}

fun <T> fire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e("哈哈哈", e.toString())
            Result.failure<T>(e)
        }
        emit(result)
    }
package com.zj.play.view.home.search

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.zj.core.util.showToast
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.room.PlayDatabase
import com.zj.play.room.dao.HotKeyDao
import com.zj.play.room.entity.HotKey
import com.zj.play.view.home.search.article.ArticleListActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    private lateinit var hotKeyDao: HotKeyDao
    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }


    override fun initData() {
        hotKeyDao = PlayDatabase.getDatabase(this).hotKeyDao()
        viewModel.hotKeyLiveData.observe(this, {
            if (it.isSuccess) {
                loadFinished()
                val hoeKey = it.getOrNull()
                if (hoeKey != null) {
                    if (viewModel.hotKey.size <= 0) {
                        viewModel.hotKey.addAll(hoeKey)
                    }
                    addFlowView()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView { initData() }
            }
        })
    }

    private fun addFlowView() {
        //往容器内添加TextView数据
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 5, 10, 15);
        if (searchFlowLayout != null) {
            searchFlowLayout.removeAllViews()
        }
        for (i in 0 until viewModel.hotKey.size) {
            val item = View.inflate(this, R.layout.layout_search_item, null)
            val tv = item.findViewById<TextView>(R.id.searchTvName)
            val delete = item.findViewById<LinearLayout>(R.id.searchLlDelete)
            val name = viewModel.hotKey[i].name
            if (viewModel.hotKey[i].order > 0) {
                delete.visibility = View.GONE
            }
            tv.text = name
            tv.setOnClickListener {
                ArticleListActivity.actionStart(this, name)
            }
            delete.setOnClickListener {
                GlobalScope.launch {
                    hotKeyDao.delete(viewModel.hotKey[i])
                }
                searchFlowLayout.removeView(item)
            }
            searchFlowLayout.addView(item, layoutParams)
        }
    }


    override fun initView() {
        searchImgBack.setOnClickListener(this)
        searchTxtRight.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.searchImgBack -> {
                finish()
            }
            R.id.searchTxtRight -> {
                val keyword = searchTxtKeyword.text.toString()
                if (TextUtils.isEmpty(keyword)) {
                    showToast("搜索关键字不能为空")
                    return
                }

                GlobalScope.launch {
                    hotKeyDao.insert(HotKey(id = -1, name = keyword))
                }
                viewModel.hotKeyLiveData
                ArticleListActivity.actionStart(this, keyword)
            }
        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

}

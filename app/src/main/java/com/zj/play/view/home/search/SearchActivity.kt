package com.zj.play.view.home.search

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zj.core.util.showToast
import com.zj.core.view.BaseActivity
import com.zj.play.R
import com.zj.play.view.home.search.article.ArticleListActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(), View.OnClickListener {

    private val viewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    private val list = arrayListOf<String>()

    override fun initData() {
        viewModel.getHotKey().observe(this, Observer {
            if (it.isSuccess) {
                loadFinished()
                val hoeKey = it.getOrNull()
                if (hoeKey != null) {
                    hoeKey.forEach { key ->
                        list.add(key.name)
                    }
                    addFlowView()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView(View.OnClickListener { initData() })
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

        for (i in 0 until list.size) {
            val tv = TextView(this)
            tv.setPadding(28, 10, 28, 10)
            tv.text = list[i]
            tv.maxEms = 10
            tv.setTextColor(resources.getColor(R.color.white))
            tv.setSingleLine()
            tv.setBackgroundResource(R.drawable.btn)
            tv.layoutParams = layoutParams
            tv.setOnClickListener {
                ArticleListActivity.actionStart(this, list[i])
            }
            searchFlowLayout.addView(tv, layoutParams)
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

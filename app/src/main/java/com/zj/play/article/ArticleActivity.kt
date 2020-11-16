package com.zj.play.article

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zj.core.util.getHtmlText
import com.zj.core.util.showToast
import com.zj.core.view.base.BaseActivity
import com.zj.model.model.CollectX
import com.zj.model.room.entity.Article
import com.zj.play.R
import com.zj.play.profile.share.ShareActivity
import kotlinx.android.synthetic.main.activity_article.*


const val PAGE_NAME = "PAGE_NAME"
const val PAGE_URL = "PAGE_URL"
const val PAGE_ID = "PAGE_ID"
const val ORIGIN_ID = "ORIGIN_ID"
const val USER_ID = "USER_ID"
const val IS_COLLECTION = "IS_COLLECTION"

class ArticleActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int = R.layout.activity_article

    private val viewModel by lazy { ViewModelProvider(this).get(ArticleViewModel::class.java) }

    private var pageName = ""
    private var pageUrl = ""
    private var pageId = -1
    private var originId = -1
    private var userId = -1
    private var isCollection = -1
    private lateinit var bottomDialogIvCollect: ImageView
    private lateinit var bottomDialogTvCollect: TextView
    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun initData() {
        pageName = intent.getStringExtra(PAGE_NAME) ?: ""
        pageUrl = intent.getStringExtra(PAGE_URL) ?: ""
        pageId = intent.getIntExtra(PAGE_ID, -1)
        isCollection = intent.getIntExtra(IS_COLLECTION, -1)
        originId = intent.getIntExtra(ORIGIN_ID, -1)
        userId = intent.getIntExtra(USER_ID, -1)
        articleTxtTitle.text = getHtmlText(pageName)
        articleWebView.loadUrl(pageUrl)
    }

    override fun initView() {
        window.setFormat(PixelFormat.TRANSLUCENT)
        articleImgBack.setOnClickListener(this)
        articleImgRight.setOnClickListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && articleWebView.canGoBack()) {
            articleWebView.goBack() //返回上个页面
            return true
        }
        return super.onKeyDown(keyCode, event) //退出H5界面
    }

    private fun setBottomDialog() {
        bottomSheetDialog = BottomSheetDialog(this)
        val behavior: BottomSheetBehavior<*> = bottomSheetDialog!!.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val dialogView: View = View.inflate(this, R.layout.dialog_bottom_sheet, null)
        bottomSheetDialog!!.setContentView(dialogView)
        val bottomDialogLlCollect =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlCollect)
        bottomDialogIvCollect = dialogView.findViewById(R.id.bottomDialogIvCollect)
        bottomDialogTvCollect = dialogView.findViewById(R.id.bottomDialogTvCollect)

        val bottomDialogLlCopy = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlCopy)
        val bottomDialogLlBrowser =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlBrowser)
        val bottomDialogLlShare = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlShare)
        val bottomDialogLlDynamic =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlDynamic)
        val bottomDialogLlReload = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlReload)
        if (isCollection == 1) {
            bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            bottomDialogTvCollect.text = getString(R.string.cancel_collection)
        } else {
            bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            bottomDialogTvCollect.text = getString(R.string.collection)
        }
        bottomDialogLlCollect.setOnClickListener(this)
        bottomDialogLlCopy.setOnClickListener(this)
        bottomDialogLlBrowser.setOnClickListener(this)
        bottomDialogLlShare.setOnClickListener(this)
        bottomDialogLlDynamic.setOnClickListener(this)
        bottomDialogLlReload.setOnClickListener(this)
        bottomSheetDialog!!.setContentView(dialogView) //给布局设置透明背景色
        ((dialogView.parent) as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog!!.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.articleImgBack -> {
                if (articleWebView.canGoBack()) {
                    //返回上个页面
                    articleWebView.goBack()
                    return
                } else {
                    finish()
                }
            }
            R.id.articleImgRight -> {
                setBottomDialog()
            }
            R.id.bottomDialogLlCollect -> {
                bottomSheetDialog?.dismiss()
                viewModel.setCollect(isCollection, pageId, originId, this) {
                    if (it) {
                        isCollection = 1
                        bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                        bottomDialogTvCollect.text = getString(R.string.cancel_collection)
                    } else {
                        isCollection = 0
                        bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        bottomDialogTvCollect.text = getString(R.string.collection)
                    }
                }
            }
            R.id.bottomDialogLlCopy -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.copyToClipboard(this, pageUrl)
                showToast(getString(R.string.copy_succeeded))
            }
            R.id.bottomDialogLlBrowser -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.jumpBrowser(this, pageUrl)
            }
            R.id.bottomDialogLlShare -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.shareUrl(this, pageUrl, pageName)
            }
            R.id.bottomDialogLlDynamic -> {
                bottomSheetDialog?.dismiss()
                ShareActivity.actionStart(this, false, userId)
            }
            R.id.bottomDialogLlReload -> {
                bottomSheetDialog?.dismiss()
                articleWebView.reload()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        articleWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        articleWebView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog?.cancel()
        articleWebView.destroy()
    }

    companion object {
        fun actionStart(
            context: Context,
            collectX: CollectX
        ) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, collectX.title)
                putExtra(PAGE_URL, collectX.link)
                putExtra(PAGE_ID, collectX.id)
                putExtra(IS_COLLECTION, 1)
                putExtra(ORIGIN_ID, collectX.originId)
                putExtra(USER_ID, collectX.userId)
            }
            context.startActivity(intent)
        }

        fun actionStart(
            context: Context,
            article: Article
        ) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, article.title)
                putExtra(PAGE_URL, article.link)
                putExtra(PAGE_ID, article.id)
                putExtra(IS_COLLECTION, article.collect)
                putExtra(USER_ID, article.userId)
            }
            context.startActivity(intent)
        }

        fun actionStart(
            context: Context,
            pageName: String,
            pageUrl: String
        ) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, pageName)
                putExtra(PAGE_URL, pageUrl)
            }
            context.startActivity(intent)
        }
    }

}

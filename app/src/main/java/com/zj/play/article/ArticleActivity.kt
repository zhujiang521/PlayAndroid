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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.zj.core.Play
import com.zj.core.util.showToast
import com.zj.core.view.BaseActivity
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
        articleTxtTitle.text =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(pageName, Html.FROM_HTML_MODE_LEGACY)
            } else {
                pageName
            }
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
        val dialogView: View = bottomSheetDialog?.layoutInflater!!.inflate(
            R.layout.dialog_bottom_sheet,
            null
        )
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
                if (isCollection == -1 || pageId == -1) {
                    showToast(getString(R.string.page_is_not_collection))
                    return
                }

                if (!Play.isLogin) {
                    showToast(getString(R.string.not_currently_logged_in))
                    return
                }

                ArticleUtils.collect(isCollection == 1, pageId, originId, this)
                if (isCollection != 1) {
                    isCollection = 1;
                    bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                    bottomDialogTvCollect.text = getString(R.string.cancel_collection)
                } else {
                    isCollection = 0;
                    bottomDialogIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    bottomDialogTvCollect.text = getString(R.string.collection)
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

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog?.cancel()
    }

    companion object {
        fun actionStart(
            context: Context,
            pageName: String,
            pageUrl: String,
            pageId: Int = -1,
            isCollection: Int = -1,
            originId: Int = -1,
            userId: Int = -1
        ) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, pageName)
                putExtra(PAGE_URL, pageUrl)
                putExtra(PAGE_ID, pageId)
                putExtra(IS_COLLECTION, isCollection)
                putExtra(ORIGIN_ID, originId)
                putExtra(USER_ID, userId)
            }
            context.startActivity(intent)
        }
    }

}

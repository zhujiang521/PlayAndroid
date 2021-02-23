package com.zj.play.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zj.core.Play
import com.zj.play.R
import com.zj.play.article.ArticleActivity
import com.zj.play.article.collect.CollectListActivity
import com.zj.play.databinding.AdapterProfileBinding
import com.zj.play.main.login.LoginActivity
import com.zj.play.profile.history.BrowseHistoryActivity
import com.zj.play.profile.rank.user.UserRankActivity
import com.zj.play.profile.user.UserActivity

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2020/5/19
 * 描述：PlayAndroid
 *
 */
class ProfileAdapter(
    private val mContext: Context,
    private val profileItemList: ArrayList<ProfileItem>,
) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    inner class ViewHolder(binding: AdapterProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        val profileAdTvTitle: TextView = binding.profileAdTvTitle
        val profileAdIv: ImageView = binding.profileAdIv
        val profileAdLlItem: LinearLayout = binding.profileAdLlItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ViewHolder {
        val binding = AdapterProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = profileItemList[position]
        with(holder) {
            profileAdTvTitle.text = data.title
            profileAdIv.setImageResource(data.imgId)
            profileAdLlItem.setOnClickListener {
                toJump(data.title)
            }
        }
    }

    private fun toJump(title: String) {
        //val dataStore = DataStoreUtils
        when (title) {
            mContext.getString(R.string.mine_points) -> {
                if (Play.isLogin) {
                    UserRankActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            mContext.getString(R.string.my_collection) -> {
                if (Play.isLogin) {
                    CollectListActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }
            mContext.getString(R.string.mine_blog) -> {
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_blog),
                    "https://zhujiang.blog.csdn.net/"
                )
//                dataStore.apply {
//                    saveSyncBooleanData(booleanKey, true)
//                    saveSyncIntData(intKey, 34)
//                    saveSyncStringData(stringKey, "我爱你啊")
//                    saveSyncFloatData(floatKey, 23f)
//
//                    saveSyncLongData(longKey, 45L)
//                }
//                GlobalScope.launch {
//                    dataStore.apply {
//
//                        saveStringData(stringKey, "我爱你啊")
//                        saveFloatData(floatKey, 23f)
//
//                        saveLongData(longKey, 45L)
//                    }
//                }
            }
            mContext.getString(R.string.browsing_history) -> {
                BrowseHistoryActivity.actionStart(mContext)
//                Log.e("ZHUJIANG", "哈哈哈")
//                val booleanData = dataStore.readBooleanData("BooleanData")
//                Log.e("ZHUJIANG", "booleanData: $booleanData")
//                val floatData = dataStore.readFloatData("FloatData")
//                Log.e("ZHUJIANG", "floatData: $floatData")
//                val intData = dataStore.readIntData("IntData")
//                Log.e("ZHUJIANG", "intData: $intData")
//                val longData = dataStore.readLongData("LongData")
//                Log.e("ZHUJIANG", "longData: $longData")
//                val stringData = dataStore.readStringData("StringData")
//                Log.e("ZHUJIANG", "stringData: $stringData")
//                Log.e("ZHUJIANG", "哈哈哈222")
//                GlobalScope.launch {
//                    dataStore.readBooleanFlow(booleanKey).first {
//                        Log.e("ZHUJIANG", "toJump111: ${it}" )
//                        true
//                    }
//                    dataStore.readIntFlow(intKey).first {
//                        Log.e("ZHUJIANG", "toJump222: ${it}" )
//                        true
//                    }
//                }

            }
            mContext.getString(R.string.mine_nuggets) -> {
                //dataStore.clearSync()
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_nuggets),
                    "https://juejin.im/user/5c07e51de51d451de84324d5"
                )
//                GlobalScope.launch {
//                    dataStores.apply {
//                        put(booleanKey, true)
//                        put(intKey, 34)
//                        put(stringKey, "我爱你啊")
//                        put(floatKey, 23f)
//                        put(longKey, 45L)
//                    }
//                }
            }
            mContext.getString(R.string.github) -> {
//                val a = dataStores.get(booleanKey, false)
//                val b = dataStores.get(intKey, 0)
//                val c = dataStores.get(stringKey, "")
//                val d = dataStores.get(floatKey, 0f)
//                val e = dataStores.get(longKey, 0L)
//                Log.e("ZHUJIANG", "toJump: $a   $b    $c    $d     $e" )
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(R.string.mine_github),
                    "https://github.com/zhujiang521"
                )
            }
            mContext.getString(R.string.about_me) -> {
                UserActivity.actionStart(mContext)
            }
        }
    }

    override fun getItemCount(): Int {
        return profileItemList.size
    }

}

data class ProfileItem(var title: String, var imgId: Int)
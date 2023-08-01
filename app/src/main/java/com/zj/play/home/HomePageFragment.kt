package com.zj.play.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.DepthPageTransformer
import com.youth.banner.transformer.ZoomOutPageTransformer
import com.zj.core.almanac.isZhLanguage
import com.zj.play.R
import com.zj.play.article.ArticleAdapter
import com.zj.play.databinding.FragmentHomePageBinding
import com.zj.play.home.almanac.AlmanacActivity
import com.zj.play.home.search.SearchActivity
import com.zj.play.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : ArticleCollectBaseFragment() {

    private val viewModel by viewModels<HomePageViewModel>()
    private var binding: FragmentHomePageBinding? = null

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater, container, attachToRoot)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            homeBanner.addBannerLifecycleObserver(viewLifecycleOwner)
            homeBanner2.addBannerLifecycleObserver(viewLifecycleOwner)
            homeBanner.setPageTransformer(ZoomOutPageTransformer())
            homeBanner2.setPageTransformer(DepthPageTransformer())
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            homeBanner.start()
            homeBanner2.start()
        }
    }

    override fun refreshData() {
        getArticleList(true)
    }

    private lateinit var bannerAdapter: ImageAdapter
    private lateinit var bannerAdapter2: ImageAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun initView() {
        binding?.apply {
            homeTitleBar.setRightImage(R.drawable.home_search_button)
            if (isZhLanguage()) {
                homeTitleBar.setTitleOnClickListener {
                    AlmanacActivity.actionStart(requireContext())
                }
            }
            homeTitleBar.setRightImgOnClickListener {
                SearchActivity.actionStart(requireContext())
                activity?.overridePendingTransition(R.anim.search_push_in, R.anim.fake_anim)
            }
            bannerAdapter = ImageAdapter(requireContext(), viewModel.bannerList)
            bannerAdapter2 = ImageAdapter(requireContext(), viewModel.bannerList2)
            homeBanner.setAdapter(bannerAdapter)
            homeBanner.setBannerRound(20f)
            homeBanner.setIndicator(CircleIndicator(context)).start()
            homeBanner2.setAdapter(bannerAdapter2)
            homeBanner2.setIndicator(CircleIndicator(context)).start()
            homeBanner2.setBannerRound(20f)
            homeToTopRecyclerView.setRecyclerViewLayoutManager(true)
            articleAdapter = ArticleAdapter(requireContext(), viewModel.articleList)
            homeToTopRecyclerView.onRefreshListener({
                page = 1
                getArticleList(true)
            }, {
                page++
                getArticleList(true)
            })
            homeToTopRecyclerView.setAdapter(articleAdapter)
        }
    }

    override fun initData() {
        startLoading()
        initBanner()
        setDataStatus(viewModel.articleLiveData, {
            if (viewModel.articleList.size > 0) loadFinished()
        }) {
            if (page == 1 && viewModel.articleList.size > 0) {
                viewModel.articleList.clear()
            }
            viewModel.articleList.addAll(it)
            articleAdapter.notifyItemInserted(it.size)
        }
        getArticleList(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initBanner() {
        setDataStatus(viewModel.getBanner(), {
            if (viewModel.bannerList.size > 0) loadFinished()
        }) {
            val main = activity as MainActivity
            if (viewModel.bannerList.size > 0)
                viewModel.bannerList.clear()
            if (viewModel.bannerList2.size > 0)
                viewModel.bannerList2.clear()
            if (main.isPort) {
                viewModel.bannerList.addAll(it)
            } else {
                for (index in it.indices) {
                    if (index / 2 == 0) {
                        viewModel.bannerList.add(it[index])
                    } else {
                        viewModel.bannerList2.add(it[index])
                    }
                }
            }
            bannerAdapter.notifyDataSetChanged()
            bannerAdapter2.notifyDataSetChanged()
        }

    }

    private fun getArticleList(isRefresh: Boolean) {
        viewModel.getArticleList(page, isRefresh)
    }

    override fun onPause() {
        super.onPause()
        binding?.apply {
            homeBanner.stop()
            homeBanner2.stop()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomePageFragment()
    }

}
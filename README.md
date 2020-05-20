# Play Android

## MVVM？瞎搞一波？

### 前言

准备写这篇文章的时候就已经后悔了，因为MVVM太火了，遍地都是教程，遍地都是完整MVVM项目源码。

之前一直使用的MVP，加上封装的LCE，感觉使用的还挺好，挺不舍得换框架的，但是这么火，不能不跟着时代走啊，再不学就要被淘汰了。

其实到现在我都没真正搞懂MVVM到底是个啥，之前的MVC、MVP都很好理解，但是MVVM还是有点懵，如果看完文章感觉我写的哪块不对的话，请在评论区告诉我，万分感激。

### 正文

前两天白嫖扔物线的协程直播课程，扔物线在里面说到，MVVM中的VM并不是ViewModel，当听完他说的之后我感觉世界观被颠覆了，我一直认为的MVVM就是Model+View+ViewModel，但现在告诉我说并不是，但没说既然不是ViewModel那么VM到底是啥，还记得之前有人说是DataBinding就是MVVM，但我看到的好多MVVM的应用中并没有使用DataBinding，就更懵逼了，其实我个人非常反感使用DataBinding，个人认为安卓好不容易才把布局和逻辑给分开，现在又要像JSP一样全写在一块，简单的页面还好，复杂页面的嵌套想想就头大，写了一个小Demo之后就放弃了。

郭神的第三行代码刚发售就赶快抢了一本签名版，里面的天气预报实战App用的就是MVVM，书里对MVVM的介绍如下：

> MVVM（Model+View+ViewModel）是一种高级项目架构模式，目前已被广泛应用在Android程序设计领域，类似的架构模式还有MVP、MVC等。简单来讲，MVVM架构可以将程序结构主要分成三部分：Model是数据模型部分；View是页面展示部分；而ViewModel比较特殊，可以将它理解成一个连接数据模型和界面展示的桥梁，从而实现让业务逻辑和界面展示分离的程序结构设计。

OK，写的很好，但是我还是没有搞懂。

这个时候又看见官方一直大力推行的JetPack，What？这又是什么鬼？这个倒是很好理解，JetPack只是官方大力推行的一个包含一系列库的总称吧，暂且理解为帮我们搭建一个MVVM项目并帮助我们简化开发流程及代码的一个系列库吧，下面来看一下官方的JetPack图，包含了很多控件：


![](https://user-gold-cdn.xitu.io/2020/5/20/17231152f97cbd37?w=2946&h=1704&f=png&s=486453)

我也照着郭神的书写了一遍书中的MVVM版的天气预报，算是对MVVM有了点理解吧，感觉就是ViewModel。。。。。。算了，先不纠结这个了，有理解的大牛帮忙在评论区分享下吧，万分感谢🙏。

然后我就在官方文档中找见了一张和郭神书中介绍MVVM时的有异曲同工之妙的图：


![](https://user-gold-cdn.xitu.io/2020/5/20/17231157bfbbd21d?w=1562&h=1276&f=png&s=176944)

看了这张图感觉又理解了，但如果问我MVVM是啥，抱歉，不知道！！！

### 开始瞎写

到目前为止，我对MVVM的理解就是在项目中不用自己搭建MVP的那一套架构，官方为你建立了许多好用的库（JetPack），可以直接进行使用，当然，要想实现LCE还是需要自己搭建的。

那么，写个啥呢？万年不变的玩安卓吧，之前学习Flutter的时候就写过一个Flutter版本的玩安卓，那就再来一个吧。

先来看一下应用截图吧：




|   ![](https://user-gold-cdn.xitu.io/2020/5/20/1723119411ffcf69?w=1080&h=1920&f=jpeg&s=193929)   |   ![](https://user-gold-cdn.xitu.io/2020/5/20/1723119fa8fc7543?w=1080&h=1920&f=jpeg&s=95032)   |
| ---- | ---- |
|   ![](https://user-gold-cdn.xitu.io/2020/5/20/172311a2c86d528c?w=1080&h=1920&f=jpeg&s=132275)   |   ![](https://user-gold-cdn.xitu.io/2020/5/20/172311a561ac9a0f?w=1080&h=1920&f=jpeg&s=80694)   |


由于篇幅问题，就不放太多图了，毕竟只是学习，也不可能多好看，详情页面的底部弹框中的图片全用的是官方自带的，因为找图太麻烦了。。。

写完整个小项目的感觉是，感觉写的不好，好多地方都可以进行优化和抽取，有空再慢慢改吧。还有就是LiveData真的挺好用，感觉它就是MVVM的核心。

### 码代码环节

既然题目说到了，那么必须实现一下，不实现下都对不起这个题目，嗯。。写点啥呢？那就写一下搜索结果页面吧，搜索页面很简单，输入需要搜索的关键字点击搜索即跳转到了搜索结果页面，搜索结果页面接收到搜索页面传过来的关键字进行搜索，很简单对吧，开始码代码。

先放一下ViewModel中的代码吧：

```kotlin
class ArticleListViewModel : ViewModel() {

    val articleList = ArrayList<Article>()

    private val pageLiveData = MutableLiveData<QueryKeyArticle>()

    val articleLiveData = Transformations.switchMap(pageLiveData) { query ->
        Repository.getQueryArticleList(query.page, query.k)
    }

    fun getArticleList(page: Int, k: String) {
        pageLiveData.value = QueryKeyArticle(page, k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)
```

再来一段Activity中的使用代码，下面再说代码的具体含义：

```kotlin
viewModel.articleLiveData.observe(this, Observer {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.articleList.size > 0) {
                        viewModel.articleList.clear()
                    }
                    viewModel.articleList.addAll(articleList.datas)
                    articleAdapter.notifyDataSetChanged()
                } else {
                    showLoadErrorView()
                }
            } else {
                showBadNetworkView(View.OnClickListener {
                    getArticleList()
                })
            }
        })
```

代码逻辑很简单，Activity中进行观察，有改变的话使用ViewModel中的getArticleList方法告诉ViewModel数据需要改变，然后通过Transformations.switchMap进行LiveData的更新，更新完之后Activit中即可收到通知，进行相应改变即可，如果有错误进行错误处理即可。

### 总结

嗯，这大概就是我理解的MVVM。

最后放一下项目Github的地址吧：[https://github.com/zhujiang521/PlayAndroid](https://github.com/zhujiang521/PlayAndroid)。

如果感觉对你有帮助，请点个Star，或者点个关注，或者点个赞也行啊，当然都点了最好😂。

哎，还是没理解MVVM，纯粹瞎写，有问题请别留情面地指出，感谢。
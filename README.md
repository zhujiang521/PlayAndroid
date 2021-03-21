# Play Android

## `Compose` 和 `Flutter`

在写完这个小 `Demo ` 之后，有一个感觉，`Compose` 和 `Flutter` 他们两个。。。。不能说有点相似，只能说完全一样！

`Compose` 完全打破了我之前对安卓的看法。。和 `Flutter` 简直是一个模子里刻出来的，连命名都有些相似，虽然 `Google` 官方说不会放弃 `Java`，但是看看协程和 `Compose`，真的想说一句：我信你个鬼，你个糟老头子坏的很！

虽然二者很像，但是发力的方向是完全不同的：

`Flutter` 大家都知道，是个 **跨平台** 的 UI 框架，注意，只是 UI 框架！各种复杂实现全都是安卓和苹果原生写的，可以一套代码多处使用，特别是现在 `Flutter 2.0` 的发布，更是不得了，支持安卓、苹果、`windows`、`mac`、网页，不得不说实在是太强了。

那 `Compose` 为什么会出现？又或者说它有什么用呢？

## `Compose` 为什么会出现

这一小节的内容很明确，写 `Compose` 为什么会出现。这个问题其实写安卓的都比较清楚，有的就算不清楚也可以猜出二三。

以前咱们编写安卓程序的时候页面都写在 `res` -> `layout` 文件夹下，以 `xml` 的形式展现，这样的好处显而易见，将逻辑代码和页面彻底分开，确实分开了，但是使用的时候又需要去 `findViewById`，`xml` 布局加载的时候又需要耗费大量时间，加载完成之后还需要通过反射来获取 View，又是一大耗时操作。。

记得之前有个大神写过一篇文章，里面通过直接 `new` 布局和 `xml` 方式写布局进行比较，速度甚至相差几十倍，然后各种各样的优化就出来了，好像有个团队甚至自己编写了一整套布局，完全没有使用 `xml`，也是神人了！

写到这里应该都知道 `Compose` 为什么会出现了吧！

## `Compose` 有啥？

`Compose` 还有一个重要的特点——声明式。

声明式？这是什么东西？怎么说呢，就是不以命令方式改变前端视图的情况下呈现应用界面，从而使编写和维护应用界面变得更加容易。

不解释还好，一解释更加懵逼。。。简单来说就是通过对数据的改变而改变布局，不用以前 findViewById 那样遍历树，减少出错的可能性，而且软件维护复杂性会随着需要更新的视图数量而增长。不行不行，我自己都有点懵了，给大家举个例子吧！

其实咱们所熟知的 `MVVM` 其实就是数据驱动布局改变的，为什么这样说呢？你想一下，你的 ViewModel 中是不是通常会定义一个 `LiveData`，然后在你的 `Activity` 或者 `Fragment` 中进行 `observe`，然后将你的 `UI` 操作放到这里，当数据改变的时候相应地去修改你的 `UI`，这样说的话是不是好理解一些呢？

## 准备重构 Compose 版本的玩安卓

这个小标题命名地不太优雅。。。凑合看吧！

这是我写的最长的一篇前言，而且好像还没解释得太明白，大家将就下吧。

其实这是我个人的一个小习惯，学习什么新东西的时候就会写个 `Demo `，之前我写过一个 `MVVM` 版的玩安卓，而且还为这个项目写过一个系列的文章，感兴趣的可以去我的文章列表看看。

这次写完官方比赛的小 `Demo ` 之后觉得 `Compose` 挺好玩，并且好多大佬都说 `Compose` 是未来的趋势，于是就想着把那个 `MVVM` 版的玩安卓改用 `Compose` 实现一下试试。

好，说干就干！

先来看看成品吧：

| ![首页](https://img-blog.csdnimg.cn/20210306141634723.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)|![项目](https://img-blog.csdnimg.cn/20210306141353699.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)
| ------------------------------------------------------------ | ------------------------------------------------------------ |
|
![我的](https://img-blog.csdnimg.cn/20210306141515614.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)|![公众号](https://img-blog.csdnimg.cn/20210306141959150.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)
|
![文章详情](https://img-blog.csdnimg.cn/20210306142938160.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)|![登录](https://img-blog.csdnimg.cn/20210306142740196.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hhb2ppYWdvdQ==,size_16,color_FFFFFF,t_70)


看着是不是也还可以？那就开始着手编写吧！

## 准备工作

在编写之前还是放一下 `Github` 地址吧：

`Github` 地址：[github.com/zhujiang521…](https://github.com/zhujiang521/PlayAndroid)

由于之前已经编写过 `MVVM` 版本的玩安卓了，所以说很多东西咱们就可以直接进行使用了，比如说一下图片资源，又比如说数据、网络请求等等都是现成了，咱们要做的只是将以前的 `xml` 布局改成 `Compose` 即可。

听着是不是很简单？但是写的时候有点懵，这还是我之前写过 Flutter 的情况下，如果大家没有写过 `Flutter` 或者 `SwiftUI` 的话看起来可能会更懵，因为里面好多东西都颠覆了我对安卓的看法。。。

为了区分和之前 `MVVM` 版本的区别，我把这次的 `Compose` 的版本分支改为了 **main** 分支，大家下载代码的时候切换下分支就可以了，或者直接下载 `main` 分支的代码也可以。

## 引入依赖

一般要使用一个新东西的第一步，来吧！

```groovy
// `Compose`
implementation "androidx.compose.ui:ui:$compose_version"
implementation "androidx.compose.runtime:runtime-livedata:$compose_version"
implementation "androidx.compose.material:material:$compose_version"
implementation "androidx.compose.ui:ui-tooling:$compose_version"
implementation "androidx.activity:activity-compose:1.3.0-alpha03"
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha02"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$compose_version"
implementation "androidx.compose.foundation:foundation:$compose_version"
implementation "androidx.compose.foundation:foundation-layout:$compose_version"
implementation "androidx.compose.material:material-icons-extended:$compose_version"

androidTestImplementation "androidx.compose.ui:ui-test:$compose_version"
androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"

// navigation
implementation "androidx.navigation:navigation-compose:1.0.0-alpha08"
```

what？不是一个 `Compose` 库吗？干嘛引入这么多？我之前也是这么想的，但是在用的时候一个个又加进去的。。。如果不知道每一个包是什么意思的话可以去官方文档中查看下，不过光看依赖名称基本就知道是什么意思了。。。

如果你也想像我一样在以前的项目中使用 `Compose`，那么下面的这一步千万别忘了，我就是忘了添加下面这一步找了整整一天的错。。。

```groovy
android {
		…………
    buildFeatures {
        `Compose` true
        viewBinding true
    }

    `Compose`Options {
        kotlinCompilerExtensionVersion compose_version
        kotlinCompilerVersion kotlin_version
    }
}
```

就是上面的，一定别忘了进行配置，不然找错误能找死。。。给我提示的是 Kotlin 内部 JVM 错误，搞得我我都准备给 `Kotlin` 提 `Bug` 了。。。

## 偷懒
本来想再写一个 README 来着，结果直接放了之前的半篇文章，大家想知道怎么写可以去我的博客，也可以直接下载代码查看，先这样

### License

```xml
Copyright (c) 2020 朱江

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```
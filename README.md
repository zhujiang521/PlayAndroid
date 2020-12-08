# Play Android

### 体验

如果看到这里想要下载尝试下的话可以直接点击链接进行下载，或者扫描下面的二维码进行下载。

[https://www.pgyer.com/llj2](https://www.pgyer.com/llj2)

![img](https://www.pgyer.com/app/qrcode/llj2)

### 玩安卓MVVM版截图

首先来看下之前已经实现的大概样式吧：

#### 初始样式

| ![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d8174415292b44f5811b85bf37c1d802~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5f31266f469b4f429edcbdc0cccfbd1c~tplv-k3u1fbpfcp-zoom-1.image) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2ade872693944653878bcdc083f95f38~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/35da6aa74259481b97b5e72274065289~tplv-k3u1fbpfcp-zoom-1.image) |

看着样式还好，但之前只是简单做了下，实现了最基本的功能，切换成横屏之后基本没办法看，更不要说分屏、夜间模式、本地缓存、无网弱网情况、无数据情况等等了，接下来看一下现在新增的适配页面吧。

#### 夜间模式

| ![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2f4675435bc14eb6b2ad3c8835eef67d~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2fb514278fe54c069105f416eb288f29~tplv-k3u1fbpfcp-zoom-1.image) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/445d5c6ae8844821bf5883a0cb550aec~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6ae7729491c14b1ebb75155a66ac7af7~tplv-k3u1fbpfcp-zoom-1.image) |

#### 横屏适配

| ![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/07e5a55a73cd4331bf8f9c4a26d9f90f~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8e0e9bbd5cfc49c5848a9e37676829cb~tplv-k3u1fbpfcp-zoom-1.image) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f28b9034bdf645c7b090f88b695ea6f3~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9534ff4ece1c41fb8cd433d7a06283de~tplv-k3u1fbpfcp-zoom-1.image) |

#### 无网、无数据适配

| ![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ec30cd391fe84d77a37b0f74be68b15a~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2fc9925bff314a71bd996f39cd99c732~tplv-k3u1fbpfcp-zoom-1.image) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/69d36313d7f845e4a1704db526d45b35~tplv-k3u1fbpfcp-zoom-1.image) | ![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0f12e1c05adf4bafae9788e8a6543506~tplv-k3u1fbpfcp-zoom-1.image) |

### 实现过程

项目用到了很多技术，基本上都是现在安卓app的最新标配，比如：MVVM、JetPack（LiveData、Room、ViewModel）、Retrofit、协程、Glide等等。

其实在优化这个Demo的时候觉得做了好多事，但是真的让我写的时候就有点懵逼了，因为用到的东西比较多，随便拎出一个来都能单独写一篇文章；

比如在apk大小的优化，这个apk最后被我优化到只剩3.3MB，现在随便一个apk都得几十MB吧。apk优化的挺多，比如代码混淆、资源文件的混淆、不需要的语言删除、图片转webp等等，如果想深入了解apk压缩的话，可以去看我之前写的这篇文章：[玩安卓必须要掌握的性能优化之APK极限压缩](https://zhujiang.blog.csdn.net/article/details/104434151)。

又比如新增的浏览历史功能中用到了**Room**，没有**Room**的时候基本都在使用原生或者使用**LitePal**和**GreenDAO**等来实现，虽然**LitePal**和**GreenDAO**也是**ORM**的方式来实现的数据库操作，但是总体来说还是没有**Room**好用，**Room**写好实现的DAO接口文件即可，在你编译的时候会直接生成对应的实现类，真的很香。

类似上面的还有很多，如果代码哪块写的不好或者有优化的地方欢迎大家告诉我或者给我提issues。

### 使用到的库

[banner](https://github.com/youth5201314/banner)

[bugly](https://bugly.qq.com/v2/workbench/apps)

[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

[utilCode](https://github.com/Blankj/AndroidUtilCode/)

[glide](https://github.com/bumptech/glide)

[retrofit](https://square.github.io/retrofit/)

dataStore

coroutines

room

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
package com.zj.core.almanac

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：11/25/20
 * 描述：activities的枚举
 *
 */
enum class ActivitiesEnum(name: String, good: String, bad: String, weekend: Boolean) {
    ACTIVITIES_1("写单元测试", "写单元测试将减少出错", "写单元测试会降低你的开发效率", false),
    ACTIVITIES_2(
        "洗澡",
        "你几天没洗澡了？",
        "会把设计方面的灵感洗掉",
        true
    ),
    ACTIVITIES_3("锻炼一下身体", "", "能量没消耗多少，吃得却更多", true),
    ACTIVITIES_4(
        "抽烟",
        "抽烟有利于提神，增加思维敏捷",
        "除非你活够了，死得早点没关系",
        true
    ),
    ACTIVITIES_5("白天上线", "今天白天上线是安全的", "可能导致灾难性后果", false),
    ACTIVITIES_6(
        "重构",
        "代码质量得到提高",
        "你很有可能会陷入泥潭",
        false
    ),
    ACTIVITIES_7("使用%t", "你看起来更有品位", "别人会觉得你在装逼", false),
    ACTIVITIES_8(
        "跳槽",
        "该放手时就放手",
        "鉴于当前的经济形势，你的下一份工作未必比现在强",
        false
    ),
    ACTIVITIES_9("招人", "你面前这位有成为牛人的潜质", "这人会写程序吗？", false),
    ACTIVITIES_10(
        "面试",
        "面试官今天心情很好",
        "面试官不爽，会拿你出气",
        false
    ),
    ACTIVITIES_11(
        "提交辞职申请",
        "公司找到了一个比你更能干更便宜的家伙，巴不得你赶快滚蛋",
        "鉴于当前的经济形势，你的下一份工作未必比现在强",
        false
    ),
    ACTIVITIES_12("申请加薪", "老板今天心情很好", "公司正在考虑裁员", false),
    ACTIVITIES_13(
        "晚上加班",
        "晚上是程序员精神最好的时候",
        "诸事不宜",
        true
    ),
    ACTIVITIES_14("在妹子面前吹牛", "改善你矮穷挫的形象", "会被识破", true),
    ACTIVITIES_15(
        "撸管",
        "避免缓冲区溢出",
        "强撸灰飞烟灭",
        true
    ),
    ACTIVITIES_16("浏览成人网站", "重拾对生活的信心", "你会心神不宁", true),
    ACTIVITIES_17(
        "吃鸡",
        "大吉大利，今晚吃鸡",
        "落地成盒",
        false
    ),
    ACTIVITIES_18(
        "写超过%l行的方法",
        "你的代码组织的很好，长一点没关系",
        "你的代码将混乱不堪，你自己都看不懂",
        false
    ),
    ACTIVITIES_19("提交代码", "遇到冲突的几率是最低的", "你遇到的一大堆冲突会让你觉得自己是不是时间穿越了", false),
    ACTIVITIES_20(
        "代码复审",
        "发现重要问题的几率大大增加",
        "你什么问题都发现不了，白白浪费时间",
        false
    ),
    ACTIVITIES_21("开会", "写代码之余放松一下打个盹，有益健康", "小心被扣屎盆子背黑锅", false),
    ACTIVITIES_22(
        "打DOTA",
        "你将有如神助",
        "你会被虐的很惨",
        true
    ),
    ACTIVITIES_23("晚上上线", "晚上是程序员精神最好的时候", "你白天已经筋疲力尽了", false),
    ACTIVITIES_24(
        "修复BUG",
        "你今天对BUG的嗅觉大大提高",
        "新产生的BUG将比修复的更多",
        false
    ),
    ACTIVITIES_25("设计评审", "设计评审会议将变成头脑风暴", "人人筋疲力尽，评审就这么过了", false),
    ACTIVITIES_26(
        "需求评审",
        "诸事不宜",
        "不过不过，重新来",
        false
    ),
    ACTIVITIES_27("上微博", "今天发生的事不能错过", "今天的微博充满负能量", true),
    ACTIVITIES_28(
        "上AB站",
        "还需要理由吗？",
        "满屏兄贵亮瞎你的眼",
        true
    ),
    ACTIVITIES_29("打王者", "今天破纪录的几率很高", "除非你想玩到把手机砸了", true);

    var names: String = ""
    var good: String? = null
    var bad: String? = null
    var weekend: Boolean? = null

    init {
        this.bad = bad
        this.good = good
        this.names = name
        this.weekend = weekend
    }
}
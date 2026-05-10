package com.example.hongguolite.data.mock

import com.example.hongguolite.data.model.Drama
import com.example.hongguolite.data.model.DramaBadge

/**
 * Mock 剧目数据。
 *
 * 现阶段所有数据写死在这里。Module 5 接入网络后，
 * 这个文件会被 Repository 的 Mock 实现替代，但数据本身保留。
 */
object MockDramas {

    val theaterList: List<Drama> = listOf(
        Drama(
            id = "drama_001",
            title = "这些房子不正常",
            coverUrl = "",  // Module 5 替换为远程 URL
            heatCount = "3741万",
            tags = listOf("悬疑", "都市"),
            badge = null
        ),
        Drama(
            id = "drama_002",
            title = "长安夜行录",
            coverUrl = "",
            heatCount = "4254万",
            tags = listOf("古装", "悬疑推理"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_003",
            title = "过年回家，和三个精神小妹挤大巴",
            coverUrl = "",
            heatCount = "4261万",
            tags = listOf("都市", "搞笑"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_004",
            title = "我的26岁女房客",
            coverUrl = "",
            heatCount = "3924万",
            tags = listOf("都市爱情", "总裁"),
            badge = null
        ),
        Drama(
            id = "drama_005",
            title = "全球冰封：开局手握百亿物资",
            coverUrl = "",
            heatCount = "4004万",
            tags = listOf("科幻末世", "都市脑洞"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_006",
            title = "大宋提刑官之夜行者",
            coverUrl = "",
            heatCount = "2891万",
            tags = listOf("古装", "悬疑"),
            badge = null
        ),
        Drama(
            id = "drama_007",
            title = "闪婚后，豪门老公他变了",
            coverUrl = "",
            heatCount = "5621万",
            tags = listOf("都市爱情", "甜宠"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_008",
            title = "绝世神医在都市",
            coverUrl = "",
            heatCount = "1532万",
            tags = listOf("都市", "逆袭", "战神"),
            badge = null
        ),
        Drama(
            id = "drama_009",
            title = "我在末世养丧尸",
            coverUrl = "",
            heatCount = "3210万",
            tags = listOf("科幻", "末世", "养成"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_010",
            title = "落花时节又逢君",
            coverUrl = "",
            heatCount = "4102万",
            tags = listOf("古装", "仙侠", "虐恋"),
            badge = null
        ),
        Drama(
            id = "drama_011",
            title = "龙王赘婿：战神归来",
            coverUrl = "",
            heatCount = "6782万",
            tags = listOf("都市", "战神", "赘婿"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_012",
            title = "重生之我是投资天才",
            coverUrl = "",
            heatCount = "2245万",
            tags = listOf("都市", "重生", "逆袭"),
            badge = null
        ),
        Drama(
            id = "drama_013",
            title = "深宫计：贵妃不好惹",
            coverUrl = "",
            heatCount = "3876万",
            tags = listOf("古装", "宫斗"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_014",
            title = "荒岛求生：我能开挂",
            coverUrl = "",
            heatCount = "1289万",
            tags = listOf("冒险", "生存", "系统"),
            badge = null
        ),
        Drama(
            id = "drama_015",
            title = "傲世丹神",
            coverUrl = "",
            heatCount = "4567万",
            tags = listOf("玄幻", "热血"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_016",
            title = "天价前妻：总裁别撩我",
            coverUrl = "",
            heatCount = "3412万",
            tags = listOf("都市爱情", "豪门"),
            badge = null
        ),
        Drama(
            id = "drama_017",
            title = "鬼吹灯之精绝古城",
            coverUrl = "",
            heatCount = "8921万",
            tags = listOf("探险", "奇幻", "悬疑"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_018",
            title = "我的师傅是仙人",
            coverUrl = "",
            heatCount = "2134万",
            tags = listOf("都市", "修仙"),
            badge = null
        ),
        Drama(
            id = "drama_019",
            title = "回到古代当富翁",
            coverUrl = "",
            heatCount = "3654万",
            tags = listOf("古装", "穿越", "种田"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_020",
            title = "极速悖论",
            coverUrl = "",
            heatCount = "1876万",
            tags = listOf("现代", "竞技"),
            badge = null
        ),
        Drama(
            id = "drama_021",
            title = "我有九个绝色师姐",
            coverUrl = "",
            heatCount = "7231万",
            tags = listOf("都市", "爽文"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_022",
            title = "大秦：开局祖龙求我出关",
            coverUrl = "",
            heatCount = "4512万",
            tags = listOf("历史", "系统"),
            badge = null
        ),
        Drama(
            id = "drama_023",
            title = "隐婚娇妻不好哄",
            coverUrl = "",
            heatCount = "2987万",
            tags = listOf("都市爱情", "甜宠"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_024",
            title = "校花的贴身高手",
            coverUrl = "",
            heatCount = "5432万",
            tags = listOf("校园", "异能"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_025",
            title = "第一狂少",
            coverUrl = "",
            heatCount = "3122万",
            tags = listOf("都市", "热血"),
            badge = null
        ),
        Drama(
            id = "drama_026",
            title = "长风渡",
            coverUrl = "",
            heatCount = "4876万",
            tags = listOf("古装", "情感"),
            badge = null
        ),
        Drama(
            id = "drama_027",
            title = "锦绣山河之将女归来",
            coverUrl = "",
            heatCount = "2654万",
            tags = listOf("古装", "励志"),
            badge = DramaBadge.NewDrama
        ),
        Drama(
            id = "drama_028",
            title = "疯狂试探：我的毒舌前夫",
            coverUrl = "",
            heatCount = "1988万",
            tags = listOf("都市爱情", "追妻火葬场"),
            badge = null
        ),
        Drama(
            id = "drama_029",
            title = "万古神帝",
            coverUrl = "",
            heatCount = "9210万",
            tags = listOf("玄幻", "系统"),
            badge = DramaBadge.HotDrama
        ),
        Drama(
            id = "drama_030",
            title = "开局抽中大奖，我成了神豪",
            coverUrl = "",
            heatCount = "2431万",
            tags = listOf("都市", "神豪", "系统"),
            badge = null
        )
    )
}
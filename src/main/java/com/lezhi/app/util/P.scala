package com.lezhi.app.util


/**
  * Created by Colin Yan on 2016/7/12.
  */
object P {

  val expressions: Array[Expr] = Array(

    /*val p1 = */ Expr("""^(.*支弄)""".r, 1)
    , /*val p2 = */ Expr("""(红旗弄|酱园弄|香家弄|木鱼弄|东河沿|界河弄|药局弄|郁家弄|鸳鸯厅弄|张家弄|祝家宅|新发展弄|小桥浜|小塔前|顾家宅|油车弄|摇荡湾弄|圣堂弄|经幢公寓|海南西弄|油车湾)""".r, 1)
    , /*val p3 = */ Expr("""(江海(新村|花园|小区))""".r, 1)
    , /*val p4 = */ Expr("""景家堰""".r, "景家堰小区")
    , /*val p5 = */ Expr("""菜花泾""".r, "菜花泾小区")
    , /*val p6 = */ Expr("""(堡镇(镇)?云海)""".r, "堡镇云海")
    , /*val p7 = */ Expr("""(青浦镇城西小区)""".r, "西部花苑一区")
    , /*val p8 = */ Expr("""(陈家镇供销社)""".r, "陈家镇供销社")
    , /*val p9 = */ Expr("""(海湾艺墅)""".r, "海湾艺墅")
    , /*val p10 = */ Expr("""(华龙别墅)""".r, "华龙别墅")
    , /*val p11 = */ Expr("""(棕榈滩别墅)""".r, "棕榈滩别墅")
    , /*val p12 = */ Expr("""(金源商厦)""".r, "金源商厦")
    , /*val p13 = */ Expr("""(宝岛别墅)""".r, "宝岛别墅")
    , /*val p14 = */ Expr("""(梦盈花园)""".r, "梦盈花园")
    , /*val p15 = */ Expr("""(广虹公寓)""".r, "广虹公寓")
    , /*val p16 = */ Expr("""(康桥花园)""".r, "康桥花园")
    , /*val p17 = */ Expr("""(惠城大厦)""".r, "惠城大厦")
    , /*val p18 = */ Expr("""(许家厅)""".r, "许家厅小区")
    , /*val p19 = */ Expr("""(祝桥工业城)""".r, "祝桥工业城")
    , /*val p20 = */ Expr("""(泖港镇中南路西侧)""".r, "泖港镇中南路西侧")
    , /*val p21 = */ Expr("""(云顶别墅)""".r, "云顶别墅")
    , /*val p22 = */ Expr("""(豪都国际花园)""".r, "上海豪都国际花园、豪都国际花园")
    , /*val p23 = */ Expr("""(玉兰花苑|建兰公寓|台兰公寓|春兰公寓)""".r, "玉兰花苑、建兰公寓、台兰公寓、春兰公寓")
    , /*val p24 = */ Expr(
      """武夷路555弄(\d+)号""".r, Array(
        BuildingNoToResidenceName(a => a >= 1 && a <= 22, "悦达花苑"),
        BuildingNoToResidenceName("路易凯旋宫")
      ))

    , /*val p25 = */ Expr("""(吴中路511弄1号)""".r, "红枫公寓")
    , /*val p26 = */ Expr("""(吴中路511弄)""".r, "古北新城")
    , /*val p27 = */ Expr("""(北京西路605弄57号)""".r, 1)
    , /*val p28 = */ Expr("""(北京西路605弄)""".r, 1)
    , /*val p29 = */ Expr("""(航华(一|二|三|四)村(一|二|三|四)街坊)""".r, 1)
    , /*val p30 = */ Expr("""(银欣花(园|苑))""".r, "银欣花苑（奉贤）")
    , /*val p31 = */ Expr("""(鹏峰小区)""".r, "鹏峰小区、鹏峰花苑")
    , /*val p32 = */ Expr("""(虹桥路2222弄33号)""".r, "王子公寓")
    , /*val p33 = */ Expr("""(虹桥路2489弄188号)""".r, "龙柏山庄")
    , /*val p34 = */ Expr("""(虹桥路2489弄222号)""".r, "满庭芳花园")
    , /*val p35 = */ Expr("""(虹桥路2489弄200号)""".r, "上海花园广场")
    , /*val p36 = */ Expr("""天钥桥路380弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(a => a >= 1 && a <= 15, "泰东新村（徐汇）"),
      BuildingNoToResidenceName(a => a >= 20 && a <= 28, "天钥花苑"),
      BuildingNoToResidenceName(a => a >= 36 && a <= 38, "南溪公寓"),
      BuildingNoToResidenceName(a => a >= 58 && a <= 60, "通汇公寓")
    ))
    , /*val p37 = */ Expr("""海江路803弄3(7|8)号""".r, "住友宝莲、住友宝莲府邸")
    , /*val p38 = */ Expr("""惠南镇新建路(\d+)幢""".r, "新建路小区")
    , /*val p39 = */ Expr("""古美路1107弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 26, 38), "新园公寓"),
      BuildingNoToResidenceName( "古美小区")
    ))
    , /*val p40 = */ Expr("""张杨路628弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 3), "东明广场"),
      BuildingNoToResidenceName( "繁荣昌盛")
    ))
    , /*val p41 = */ Expr("""""中山南一路500弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 1), "丽都大厦"),
      BuildingNoToResidenceName( "香港新世界花园")
    ))
    , /*val p42 = */ Expr("""""定西路1310弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 2, 2), "南天大厦"),
      BuildingNoToResidenceName( "长乐大厦（长宁）")
    ))
    , /*val p43 = */ Expr("""汶水东路650弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 2), "华东公寓"),
      BuildingNoToResidenceName( "水电小区")
      ))
    , /*val p44 = */ Expr("""西凌家宅路111弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 3, 4), "庆隆大楼、科凌公寓"),
      BuildingNoToResidenceName( "西凌新村")
    ))
    , /*val p45 = */ Expr("""仙霞路620弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.in(_, 30, 32, 34), "虹古大楼"),
      BuildingNoToResidenceName( "虹霞小区")
    ))
    , /*val p46 = */ Expr("""龙华西路31弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 51), "俞一小区"),
      BuildingNoToResidenceName( "海波花苑")
))    , /*val p47 = */ Expr("""新华路569弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 50, 60), "静怡村"),
      BuildingNoToResidenceName(Expr.between(_, 78, 88), "静怡村"),
      BuildingNoToResidenceName(Expr.in(_, 77), "鉴赏新华"),
      BuildingNoToResidenceName(Expr.in(_, 99, 158), "当代新华")
))    , /*val p48 = */ Expr("""大渡河路1332弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 2), "银巷公寓、开开公寓、银开小区"),
      BuildingNoToResidenceName("邮电三村")
))    , /*val p49 = */ Expr("""长岛路85弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 89), "沪南小区"),
      BuildingNoToResidenceName("船舶小区")
))    , /*val p50 = */ Expr("""成山路668弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 21, 23), "恒大星级公寓"),
      BuildingNoToResidenceName(Expr.between(_, 35, 58), "恒大星级公寓"),
      BuildingNoToResidenceName("成山小区")
))    , /*val p51 = */ Expr("""碧云路86弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 13), "金洋十方庭"),
      BuildingNoToResidenceName("罗山花苑")
))    , /*val p52 = */ Expr("""昌平路428弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 10), "贤居天下苑、贤居天下"),
      BuildingNoToResidenceName("静安行家、东海园")
))    , /*val p53 = */ Expr("""罗山路1700弄(\d+)号""".r, Array(
      BuildingNoToResidenceName(Expr.between(_, 1, 8), "天合金桂家园"),
      BuildingNoToResidenceName("锦绣小区")
))    , /*val p54 = */  null // """(三林路1662弄(\d+)号)""".r
    , /*val p55 = */ null // """张杨路1662弄(\d+)号""".r

    , /*val p56 = */ null //"""长顺路(\d+)号""".r
    , /*val p57 = */ null //"""(昭化路(\d+)号)""".r
    , /*val p58 = */ null //"""青年路(\d+)号""".r
    , /*val p59 = */ null //"""南码头路1521弄(\d+)号""".r
    , /*val p60 = */ null //"""大木桥路340弄(\d+)号""".r
    , /*val p61 = */ null //"""程家桥路80弄(?:\d+幢)?(\d+)号""".r
    , /*val p62 = */ null //"""(龙漕路51弄(\d+)号)""".r
    , /*val p63 = */ null //"""水上新村""".r
    , /*val p64 = */ null //"""^(?:杨园镇)镇南路(\d+)号""".r
    , /*val p65 = */ null //"""东体育会路(\d+)号""".r
    , /*val p66 = */ null //"""(八一路599)""".r, "宝岛世纪园")
    , /*val p67 = */ null //"""(西凌家宅路111弄3号)""".r, "庆隆大楼")
    , /*val p68 = */ null //"""怒江路651弄(\d+)号""".r
    , /*val p69 = */ null //"""万航渡路858弄(\d+)号""".r
    , /*val p70 = */ null //"""浦东大道1695弄(\d+)号""".r
    , /*val p71 = */ null //"""金桥(?:镇?)横街(\d+)号""".r
    , /*val p72 = */ null //"""棋盘路998弄""".r, "菊园新村嘉宁坊")
    , /*val p73 = */ Expr("""(嘉.*?坊)""".r, 1)
    , /*val p74 = */ Expr("""(五四农场海城)\d+区""".r, 1)
    , /*val p75 = */ Expr("""(静安新城\d+区)""".r, 1)
    , /*val p76 = */ Expr("""(张杨路1662弄[89]号)""".r, 1)
    , /*val p77 = */ Expr("""(漕宝路1467弄[0-9一二三四五六七八九十]+区)""".r, 1)
    , /*val p78 = */ Expr("""(贝港(东|西|南|北)区)""".r, 1)
    , /*val p79 = */ Expr("""(迎园(新村)?[0-9一二三四五六七八九十]+坊)""".r, 1)
    , /*val p80 = */ Expr("""(管弄路\d+弄)""".r, 1)
    , /*val p81 = */ Expr("""(西河沿\d+号)""".r, 1)
    , /*val p82 = */ Expr("""^(.*弄)""".r, 1)
    , /*val p83 = */ null //"""^(.*?坊)""".r
    , /*val p84 = */ Expr("""^(.*?(村|宅))\d+""".r, 1)
    , /*val p85 = */ Expr("""^(.*?(路|街|道)(.*?)号)""".r, 1)
    , /*val p86 = */ Expr("""^(.*?)((（.*?）)?[0-9a-z]+)幢""".r, 1)
    , /*val p87 = */ Expr("""^(.*?(苑|公寓|楼|小区|坊|园))\d+""".r, 1)
    , /*val p88 = */ Expr("""(奉贤区泰日|启航城|南林家港|二纺工房|翠亭别墅|巨龙台湾城|小西泯沟)""".r, 1)
    , /*val p89 = */ Expr("""((小塔前|后河沿|青松石)\d+号)""".r, 1)
    , /*val p90 = */ Expr("""^(.*?镇教工(住宅)?楼)""".r, 1)
    , /*val p91 = */ Expr("""^(.*?(村))""".r, 1)
    , /*val p92 = */ Expr("""二纺新工房|凇兴西路176号""".r, "二纺新工房")
    , /*val p93 = */ Expr("""八一路719""".r, "八一路719号")
    , /*val p94 = */ Expr("""寒山寺158号""".r, "寒山寺路158号")
    , /*val p95 = */ Expr("""(西小港\d+号)""".r, 1)
    , /*val p96 = */ null //"""新崇北路397(.*?)(\d+)""".r
    , /*val p97 = */ Expr("""(油车湾\d+号)""".r, 1)
    , /*val p98 = */ Expr("""城中南路158""".r, "城中南路158号")
    , /*val p99 = */ Expr("""大团镇食品购销站家畜良种场""".r, "大团镇食品购销站家畜良种场")
    , /*val p100 = */ Expr("""淀湖山庄|淀山湖山庄""".r, "淀湖山庄")
    , /*val p101 = */ Expr("""(徐家桥19号)""".r, 1)
    , /*val p102 = */ Expr("""(东合德里47号)""".r, 1)
    , /*val p103 = */ null //"""东江(.*?)(\d+)号""".r
    , /*val p104 = */ Expr("""(东门新6号)""".r, 1)
    , /*val p105 = */ Expr("""(东西巷24号)""".r, 1)
    , /*val p106 = */ Expr("""(杜家滩)""".r, 1)
    , /*val p107 = */ Expr("""(堆栈)""".r, 1)
    , /*val p108 = */ Expr("""(敦昌花园)""".r, 1)
    , /*val p109 = */ Expr("""(盐敖)""".r, 1)
    , /*val p110 = */ Expr("""(^泰日(\d+)$|奉贤县泰日)""".r, "泰日")
    , /*val p111 = */ Expr("""(佛字桥18号)""".r, 1)
    , /*val p112 = */ Expr("""(虹梅路3131)""".r, 1)
    , /*val p113 = */ Expr("""(富田花苑)""".r, 1)
    , /*val p114 = */ Expr("""(古华(南|A|B)区)""".r, 1)
    , /*val p115 = */ Expr("""(谷阳北106号)""".r, 1)
    , /*val p116 = */ null //"""(城桥镇寒山寺路234)""".r
    , /*val p117 = */ Expr("""(鹤祥路401)""".r, "鹤祥路401弄")
    , /*val p118 = */ Expr("""(新民镇5号)""".r, 1)
    , /*val p119 = */ Expr("""(新民镇教师公房)""".r, 1)
    , /*val p120 = */ Expr("""(红宾院)""".r, 1)
    , /*val p121 = */ Expr("""后河沿201、203、205、207号|后河沿201号|后河沿203号|后河沿205号|后河沿207号""".r, "后河沿201、203、205、207号")
    , /*val p122 = */ Expr("""(方泰733号)""".r, 1)
    , /*val p123 = */ Expr("""(靖海桥\d+号)""".r, 1)
    , /*val p124 = */ Expr("""(油车场\d+号)""".r, 1)
    , /*val p125 = */ Expr("""(界泾港\d+号)""".r, 1)
    , /*val p126 = */ Expr("""(锦绣公寓|锦轩新墅|金沙滩|瞿家浜38号|瞿家浜45号|兰芬堂30号)""".r, 1)
    , /*val p127 = */ Expr("""(聚丰园路\d{3}室)""".r, 1)
    , /*val p128 = */ Expr("""((?:军工路)?周家\d+号)""".r, 1)
    , /*val p129 = */ Expr("""(里仁桥34号|练新西路供销大楼|燎原场部|绿地丽水名邸|人民医院3号)""".r, 1)
    , /*val p130 = */ null //"""(石化临潮(一|二|三))""".r
    , /*val p131 = */ Expr("""(凌桥镇\d+号)""".r, 1)
    , /*val p132 = */ Expr("""(崂山西路160507室)""".r, "崂山西路160号")
    , /*val p133 = */ Expr("""(浦东大道2554)""".r, "浦东大道2554号")
    , /*val p134 = */ Expr("""(浦东大道435|乐安镇31号|张桥镇永业路603室|北胜浜)""".r, 1)
    , /*val p135 = */ Expr("""(青东\d+号)""".r, 1)
    , /*val p136 = */ Expr("""(重固镇\d{3}室)""".r, "北青公路9322弄")
    , /*val p137 = */ Expr("""(龙溪小区花园别墅)""".r, "凤溪镇凤溪龙溪小区花园别墅")
    , /*val p138 = */ Expr("""(金泽镇大新街南侧)""".r, "金泽镇大新街南侧")
    , /*val p139 = */ Expr("""(湖燕)""".r, "青浦镇城东湖燕大楼")
    , /*val p140 = */ Expr("""(城中南路综合楼|崧泽别墅|凤溪.*?银行大楼|商榻镇小商品市场|庆华新海)""".r, 1)
    , /*val p141 = */ Expr("""(虬江码头\d+号)""".r, 1)
    , /*val p142 = */ Expr("""(三召子|商蔡路商业住宅楼|蔡路银信大楼|高桥镇31号)""".r, 1)
    , /*val p143 = */ Expr("""(控江1531号)""".r, "控江路1531号")
    , /*val p144 = */ Expr("""(青松公路桥河南)""".r, "青松公路桥河南")
    , /*val p145 = */ Expr("""(沈家桥(\d+)号)""".r, 1)
    , /*val p146 = */ Expr("""(竖新镇2号|竖新镇3号|竖新镇5号)""".r, 1)
    , /*val p147 = */ Expr("""(洪庙镇水闸路7)""".r, "洪庙镇水闸路7-9号")
    , /*val p148 = */ Expr("""(沈家桥6号|大仓桥南滩8号|大涨泾64号|花楼巷17号)""".r, 1)
    , /*val p149 = */ Expr("""(里仁桥\d+号)""".r, 1)
    , /*val p150 = */ Expr("""(西佘山26号|环城路145室|西新桥25号|大涨泾21号|仑城21号)""".r, 1)
    , /*val p151 = */ Expr("""(邱家湾58号|小桥浜14号|谈家巷2号|谭家桥40号)""".r, 1)
    , /*val p152 = */ Expr("""(塘子泾124号|塘子泾125号|塘子泾126号)""".r, "塘子泾124号、塘子泾125号、塘子泾126号")
    , /*val p153 = */ Expr("""(天目西路198)""".r, "天目西路198号")
    , /*val p154 = */ Expr("""天鹏四区""".r, "天鹏四区")
    , /*val p155 = */ Expr("""(望新504号|万寿新54号|五厍镇五厍街)""".r, 1)
    , /*val p156 = */ Expr("""(西沙洪浜\d+号)""".r, 1)
    , /*val p157 = */ null //"""(西小港(\d+)-(\d+)号)""".r
    , /*val p158 = */ Expr("""(西引路3086)""".r, "西引路308-1号")
    , /*val p159 = */ Expr("""(香花桥镇(住宅)?南区(?:23|31)号)""".r, "香花桥镇南区")
    , /*val p160 = */ Expr("""(香花桥镇网点住宅)""".r, "香花桥镇网点住宅")
    , /*val p161 = */ Expr("""(详见附记308-322号)""".r, "详见附记308-322号")
    , /*val p162 = */ Expr("""新崇中路57$""".r, "新崇中路57-11号")
    , /*val p163 = */ Expr("""(小桥浜\d+号)""".r, 1)
    , /*val p164 = */ Expr("""(新泾乡一号地块|秀野桥滩37号|学前二邨(?:29|30|31)号|杨泰二53号|松花江287号|西藏南路765|油车湾109号)""".r, 1)
    , /*val p165 = */ Expr("""(榆树头(26|29|31|34|35)号)""".r, 1)
    , /*val p166 = */ Expr("""(育秀公寓|育秀西区)""".r, 1)
    , /*val p167 = */ Expr("""(闸园(新)?(村)?别墅)""".r, "闸园别墅")
    , /*val p168 = */ Expr("""(泖肖公路西侧)""".r, "泖肖公路西侧")
    , /*val p169 = */ Expr("""(赵沟浜\d+号|东江5号|蒸淀镇供销社住宅楼)""".r, 1)
    , /*val p170 = */ Expr("""(陆家浜号\d+号)""".r, "青年汇中福花苑")
    , /*val p171 = */ Expr("""(泖港中南路西侧|赵沟浜(?:6|8)号|南八灶(?:162|165)号)""".r, 1)
    , /*val p172 = */ Expr("""(西湖街94|朱家廊2号|紫苑小区|方泰镇700号|东江13号|重庆北路176、180、188|虹中442号)""".r, 1)
    , /*val p173 = */ Expr("""(朱家滩|朱介滩)""".r, "朱家滩小区")
    , /*val p174 = */ Expr("""(佛字桥花楼巷7号|钱门621号|界浜场33号|前进农场供应站)""".r, 1)
    , /*val p175 = */ Expr("""(人寿桥6号)""".r, 1)
    , /*val p176 = */ Expr("""(仁德路67601)""".r, "仁德路67弄")
    , /*val p177 = */ Expr("""(唐安501|虬江码头13甲号|七浦路2209|凉城路918、920、922、924、926、928)""".r, 1)
    , /*val p178 = */ Expr("""殷行622号""".r, "殷行路622号")
    , /*val p179 = */ Expr("""(城桥镇西门12队|东方子桥32号|燎原厂1号|前进农场医院生活区1号)""".r, 1)
    , /*val p180 = */ Expr("""(凤凰镇7号|西方子桥102号|东八灶58号|石湖荡镇河南全幢|新民镇教师工房5号|燎原农场场部|白鹤镇镇北路)""".r, 1)
    , /*val p181 = */ null //"""中山北路2605弄(//d+)号""".r
  )

}
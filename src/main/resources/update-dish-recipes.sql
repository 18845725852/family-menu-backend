-- 按菜名更新制作方法。一行一步，前端会按换行展示。
UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鸡翅洗净划两刀，用盐、生抽、料酒、姜片腌 20 分钟',
  '热锅少油，鸡翅煎至两面金黄',
  '倒入可乐至没过鸡翅，加生抽、老抽调色',
  '中小火焖 15 分钟，大火收汁即可'
) WHERE name = '可乐鸡翅';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '牛肉切块焯水去血沫',
  '白萝卜滚刀块备用',
  '热油爆香葱姜，下牛肉炒至变色',
  '加生抽、老抽、料酒和热水，小火炖 40 分钟',
  '下萝卜继续炖至软烂，调盐出锅'
) WHERE name = '萝卜炖牛肉';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '牛腩切块焯水',
  '番茄切块，葱姜蒜切好',
  '热油炒香牛腩，加番茄炒出汁',
  '加生抽、料酒和热水，小火炖 50 分钟',
  '汤汁浓稠后加盐、糖调味'
) WHERE name = '番茄炖牛腩';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '牛肉切块焯水',
  '土豆切滚刀块',
  '热油爆香葱姜，下牛肉煸炒',
  '加生抽、老抽、八角和热水炖 30 分钟',
  '下土豆炖至软糯，收汁出锅'
) WHERE name = '土豆烧牛肉';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鸡块洗净，用盐、料酒、生抽腌 15 分钟',
  '热油把鸡块煎至表面金黄',
  '加葱姜、生抽、老抽、糖和热水',
  '焖 15 分钟后大火收汁'
) WHERE name = '红烧鸡块';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '蒜薹去老根切段，猪肉切片腌一下',
  '热油滑炒肉片盛出',
  '蒜薹下锅炒至翠绿',
  '倒回肉片，加盐、生抽快速翻匀'
) WHERE name = '蒜薹炒肉';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '猪肉切丝，用料酒、淀粉抓匀',
  '青椒切丝',
  '热油滑炒肉丝盛出',
  '青椒炒断生，倒回肉丝加盐、生抽翻匀'
) WHERE name = '青椒肉丝';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '排骨斩段焯水',
  '热油煎至微黄，爆香葱姜',
  '倒入可乐没过排骨，加生抽、老抽',
  '小火焖 25 分钟，大火收汁'
) WHERE name = '可乐排骨';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '肉片用盐、淀粉腌一下',
  '杏鲍菇切片',
  '热油滑炒肉片盛出',
  '杏鲍菇炒出香味，倒回肉片加盐、生抽翻匀'
) WHERE name = '杏鲍菇炒肉片';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '腐竹提前泡软切段',
  '五花肉切片煸出油',
  '加生抽、老抽、糖炒上色',
  '下腐竹加少量水焖 10 分钟，收汁即可'
) WHERE name = '腐竹烧肉';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '肉片用盐、料酒、淀粉腌好',
  '配菜切好：豆芽、生菜、蒜苗',
  '热油滑熟肉片盛出',
  '炒香干辣椒、花椒和豆瓣酱',
  '下配菜炒熟，倒回肉片，淋上水淀粉和热油'
) WHERE name = '水煮肉片';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鸡蛋打散，番茄切块',
  '热油炒蛋至凝固盛出',
  '少油炒软番茄，加少许盐和糖',
  '倒回鸡蛋翻匀，出锅前可加点香葱'
) WHERE name = '番茄炒蛋';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '包菜撕成大片洗净沥干',
  '热锅少油，放入干辣椒和蒜片',
  '下包菜大火快炒',
  '加盐、生抽、陈醋迅速翻匀出锅'
) WHERE name = '手撕包菜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '土豆切细丝，清水浸泡去淀粉',
  '青椒切丝',
  '热油爆香干辣椒和蒜',
  '下土豆丝大火炒至透明，加青椒、盐、醋翻匀'
) WHERE name = '青椒土豆丝';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '青菜洗净沥干',
  '热油爆香蒜末',
  '下青菜大火快炒至软塌',
  '加盐调味即可'
) WHERE name = '清炒青菜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '油麦菜洗净切段沥干',
  '蒜末用热油爆香',
  '下油麦菜大火翻炒',
  '加盐、少许生抽迅速出锅'
) WHERE name = '蒜蓉油麦菜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '茄子、土豆、青椒切块',
  '茄子先炸或煎至变软盛出',
  '土豆炒至微黄，下青椒',
  '倒回茄子，加蒜末、生抽、糖翻匀收汁'
) WHERE name = '地三鲜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鸡蛋打散，青椒切圈或切丝',
  '热油炒蛋盛出',
  '青椒炒出香味',
  '倒回鸡蛋，加盐翻匀'
) WHERE name = '青椒炒蛋';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '西葫芦切片或切丝',
  '热油爆香蒜末',
  '下西葫芦大火快炒',
  '加盐，炒至断生出锅，不要炒太久'
) WHERE name = '清炒西葫芦';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '香菇切片，青菜洗净',
  '热油炒香香菇',
  '下青菜一起翻炒',
  '加盐、少许生抽调味'
) WHERE name = '香菇青菜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '杏鲍菇切片，青椒切块',
  '热油先炒杏鲍菇至出水',
  '下青椒炒匀',
  '加盐、生抽快速翻炒出锅'
) WHERE name = '杏鲍菇炒青椒';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '豆腐切片，撒少许盐',
  '平底锅刷油，小火慢煎',
  '两面煎至金黄',
  '可淋少许生抽或椒盐即可'
) WHERE name = '香煎豆腐';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '腐竹泡软切段',
  '热油爆香蒜末和辣椒',
  '下腐竹翻炒',
  '加盐、生抽炒入味'
) WHERE name = '清炒腐竹';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '豆角洗净掰段，擦干水分',
  '热油下豆角煸至表皮起皱',
  '加蒜末、干辣椒继续翻炒',
  '加盐、生抽炒匀出锅'
) WHERE name = '干煸豆角';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '四季豆去筋切段，可先焯水',
  '热油爆香蒜末',
  '下四季豆大火翻炒',
  '加盐调味，炒至断生'
) WHERE name = '清炒四季豆';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '虾剪须去虾线，用料酒、盐腌 10 分钟',
  '蒜切末',
  '热油爆香蒜末，下虾大火翻炒',
  '加少许盐和生抽，炒至虾身卷红即可'
) WHERE name = '蒜蓉虾';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '虾处理干净',
  '锅中加水、姜片、料酒烧开',
  '下虾煮至变红，约 2 分钟',
  '捞出配蒜蓉酱油或蘸料食用'
) WHERE name = '白灼大虾';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '虾洗净，用料酒腌一下',
  '热油煎虾至变色',
  '倒入啤酒没过虾身，加葱姜',
  '焖 5 分钟收汁，撒葱花出锅'
) WHERE name = '啤酒焖虾';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鱼片用盐、料酒、淀粉腌好',
  '酸菜切丝，金汤用番茄或酸汤底',
  '锅中炒香酸菜，加水烧开',
  '下鱼片轻轻拨散，煮至变白',
  '加盐调味，可撒葱花和白胡椒'
) WHERE name = '金汤酸菜鱼';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '梭子蟹刷洗干净，切开去鳃',
  '热油爆香葱姜蒜和辣椒',
  '下蟹块大火翻炒',
  '加生抽、料酒和少量水焖 8 分钟',
  '收汁后撒葱段出锅'
) WHERE name = '红烧梭子蟹';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '大闸蟹刷洗干净',
  '肚脐朝上放入盘中，放姜片',
  '水开后上锅蒸 15 到 20 分钟',
  '配姜醋蘸料食用'
) WHERE name = '清蒸大闸蟹';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '花甲用盐浸泡吐沙，洗净',
  '热油爆香蒜、辣椒、豆豉',
  '下花甲大火快炒',
  '加料酒，盖盖焖至开口即可'
) WHERE name = '爆炒花甲';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鱿鱼须洗净，用料酒腌一下',
  '热油爆香蒜和辣椒',
  '下鱿鱼须大火快炒至卷起',
  '加盐、生抽迅速出锅，不要炒老'
) WHERE name = '爆炒鱿鱼须';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '黄瓜拍碎切段',
  '加蒜末、盐、生抽、醋、香油',
  '拌匀腌 5 分钟即可'
) WHERE name = '凉拌黄瓜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '豆角洗净焯至断生，过凉切段',
  '加蒜末、盐、生抽、醋、辣椒油',
  '拌匀即可'
) WHERE name = '凉拌豆角';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '毛豆洗净加盐焯熟，过凉',
  '可剥壳或连壳拌',
  '加蒜末、生抽、醋、香油拌匀'
) WHERE name = '凉拌毛豆';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '花生焯水或煮熟过凉',
  '加盐、味精、香菜、辣椒油',
  '拌匀即可冷食'
) WHERE name = '凉拌花生';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '海带丝洗净焯水过凉',
  '加蒜末、醋、生抽、糖、辣椒油',
  '拌匀腌一会儿更入味'
) WHERE name = '凉拌海带丝';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '蒜薹洗净，用刀拍松切段',
  '加蒜末、盐、生抽、醋、香油',
  '拌匀即可'
) WHERE name = '拍蒜薹';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '牛肉整块冷水下锅，加葱姜料酒焯去血沫',
  '另起锅加水、八角、桂皮、香叶、生抽、老抽、糖',
  '下牛肉小火卤 1 小时',
  '关火浸泡更入味，晾凉切片'
) WHERE name = '卤牛肉';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '排骨焯水',
  '玉米切段',
  '排骨、玉米、姜片冷水下锅',
  '大火烧开转小火炖 40 分钟，加盐调味'
) WHERE name = '玉米排骨汤';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '牛腩切块焯水',
  '萝卜切滚刀块',
  '牛腩、姜片加水炖 40 分钟',
  '下萝卜再炖 20 分钟，加盐出锅'
) WHERE name = '萝卜牛腩汤';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '排骨焯水',
  '海带泡发切条',
  '排骨、海带、姜片加水炖 40 分钟',
  '加盐调味即可'
) WHERE name = '海带排骨汤';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '鸡块焯水',
  '菌菇去根洗净',
  '鸡块、姜片加水炖 30 分钟',
  '下菌菇再炖 10 分钟，加盐出锅'
) WHERE name = '菌菇鸡汤';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '大米淘洗 1 到 2 遍',
  '米和水按约 1:1.2 下锅',
  '煮开后转小火焖 15 分钟',
  '焖好再焖 5 分钟再开盖'
) WHERE name = '米饭';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '冷饭搓散，鸡蛋打散',
  '热油炒蛋盛出',
  '下冷饭炒散，倒回鸡蛋',
  '加盐、生抽炒匀，可撒葱花'
) WHERE name = '蛋炒饭';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '面粉加酵母和水揉成光滑面团',
  '发酵至两倍大',
  '分成剂子做成馒头生坯，再醒 15 分钟',
  '上锅蒸 15 分钟，关火再焖 3 分钟'
) WHERE name = '馒头';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '清水烧开，下入面条',
  '煮至软硬合适',
  '捞出过凉或直接浇汤、拌酱食用'
) WHERE name = '面条';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '清水大火烧开',
  '下饺子，轻轻推动防粘锅',
  '再次沸腾后加一小碗凉水，重复两次',
  '饺子浮起后即可捞出'
) WHERE name = '水饺';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '玉米去衣洗净',
  '冷水下锅，水开后再煮 10 到 15 分钟',
  '捞出可直接吃，或刷少许盐和黄油'
) WHERE name = '玉米';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '发面或现成饼坯刷油',
  '平底锅小火慢烙',
  '两面烙至金黄鼓起即可'
) WHERE name = '烧饼';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '西瓜洗净外表',
  '切开去籽切块',
  '装盘冷藏后更好吃'
) WHERE name = '西瓜';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '葡萄洗净，可加盐轻搓再冲净',
  '沥干装盘',
  '冰镇后口感更好'
) WHERE name = '葡萄';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '橘子洗净',
  '剥皮去筋络',
  '直接食用或掰瓣装盘'
) WHERE name = '橘子';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '龙眼去壳去核',
  '可直接吃，或冰镇后食用',
  '不宜一次吃太多'
) WHERE name = '龙眼';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '冷藏冰镇',
  '开罐或倒入杯中直接饮用'
) WHERE name = '可乐';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '冷藏冰镇',
  '开罐或倒入杯中直接饮用'
) WHERE name = '雪碧';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '摇匀后倒入杯中',
  '可加冰块',
  '尽快饮用口感更好'
) WHERE name = '橙汁';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '冷藏后倒入杯中',
  '可直接饮用，或加热后饮用'
) WHERE name = '牛奶';

UPDATE family_menu.dishes SET recipe = CONCAT_WS(CHAR(10),
  '冰镇后开瓶',
  '倒入杯中饮用，配餐更合适'
) WHERE name = '啤酒';

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `inviter_id` bigint NOT NULL COMMENT '邀请人id',
  `invitee_id` bigint NOT NULL COMMENT '被邀请人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邀请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model
-- ----------------------------
DROP TABLE IF EXISTS `model`;
CREATE TABLE `model`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模型id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `cover_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片url',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `vae` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首选vae',
  `sampler` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首选采样器',
  `hr_upscaler` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首选高清化算法',
  `default_prompt` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '默认正向描述词',
  `default_negative_prompt` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '默认反向描述词',
  `lora` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '搭配lora',
  `priority` int NOT NULL COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 0 COMMENT '是否启用1是0否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pic
-- ----------------------------
DROP TABLE IF EXISTS `pic`;
CREATE TABLE `pic`  (
  `id` bigint NOT NULL,
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `url_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址uuid',
  `request_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求uuid',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `task_id` bigint NOT NULL COMMENT '任务id',
  `model_id` int NOT NULL COMMENT '模型id',
  `parent_pic_id` bigint NULL DEFAULT NULL COMMENT '源图id',
  `reference_pic_id` bigint NULL DEFAULT NULL COMMENT '参考图id',
  `status` int NOT NULL COMMENT '状态 0生成中 1已生成 2废弃',
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型',
  `prompt` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正向描述词',
  `negative_prompt` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反向描述词',
  `translated_prompt` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '正向描述词英文翻译',
  `translated_negative_prompt` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '反向描述词英文翻译',
  `quality` int NULL DEFAULT NULL COMMENT '质量',
  `ratio` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '比例',
  `width` int NOT NULL COMMENT '宽',
  `height` int NOT NULL COMMENT '高',
  `seed` bigint NULL DEFAULT NULL COMMENT '种子',
  `subseed` bigint NULL DEFAULT NULL COMMENT '子种子',
  `model_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型编码',
  `vae` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'vae',
  `sampler_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '采样器',
  `steps` int NOT NULL COMMENT '采样步数 10-30',
  `cfg` decimal(3, 1) NOT NULL COMMENT '提示词引导系数 1-30 步进0.5',
  `enable_hr` int NOT NULL COMMENT '启用高分辨率修复',
  `hr_upscaler` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '高清化算法',
  `denoising_strength` decimal(3, 2) NULL DEFAULT NULL COMMENT '重绘强度 0.00-1.00之间两位小数',
  `hr_scale` decimal(3, 2) NULL DEFAULT NULL COMMENT '放大倍数 1-4之间两位小数 步进0.05',
  `enable_extra` int NULL DEFAULT NULL COMMENT '是否启用工序三高清化extra',
  `upscaling_resize` int NULL DEFAULT NULL COMMENT '高清化extra放大倍数 1-4',
  `alwayson_scripts` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展脚本参数',
  `infotexts` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '通用图片描述文本',
  `is_user_delete` int NOT NULL DEFAULT 0 COMMENT '用户是否删除1是0否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uuid`(`uuid` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pic_share
-- ----------------------------
DROP TABLE IF EXISTS `pic_share`;
CREATE TABLE `pic_share`  (
  `id` bigint NOT NULL,
  `pic_id` bigint NOT NULL COMMENT '图片id',
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片uuid',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作品标题',
  `hits` int NOT NULL COMMENT '点击量',
  `like_count` int NOT NULL COMMENT '点赞量',
  `draw_count` int NOT NULL COMMENT '画同款次数',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uuid`(`uuid` ASC) USING BTREE,
  INDEX `idx_picid`(`pic_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图片分享表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for point_record
-- ----------------------------
DROP TABLE IF EXISTS `point_record`;
CREATE TABLE `point_record`  (
  `id` bigint NOT NULL,
  `request_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求uuid',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `daily_point` int NOT NULL COMMENT '增减的每日积分',
  `point` int NOT NULL COMMENT '增减的永久积分',
  `operation_type` int NOT NULL COMMENT '增加为1，减少为0',
  `operation_source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作来源',
  `point_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '积分类型',
  `return_daily_point` int NOT NULL DEFAULT 0 COMMENT '返还的每日积分',
  `return_point` int NOT NULL DEFAULT 0 COMMENT '返还的永久积分',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id_operation_source`(`user_id` ASC, `operation_source` ASC) USING BTREE,
  INDEX `idx_request_uuid`(`request_uuid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for request_limit_log
-- ----------------------------
DROP TABLE IF EXISTS `request_limit_log`;
CREATE TABLE `request_limit_log`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `uri` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求接口',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请求限制记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for request_log
-- ----------------------------
DROP TABLE IF EXISTS `request_log`;
CREATE TABLE `request_log`  (
  `id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip',
  `method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式',
  `uri` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求接口',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `elapsed_time` int NOT NULL COMMENT '请求耗时',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请求日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_record`;
CREATE TABLE `sign_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `sign_date` date NOT NULL COMMENT '签到日期',
  `sign_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userid_signdate`(`user_id` ASC, `sign_date` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '签到记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for spellbook
-- ----------------------------
DROP TABLE IF EXISTS `spellbook`;
CREATE TABLE `spellbook`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文描述',
  `prompt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文prompt',
  `title` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `subtitle` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子标签',
  `visible` int NOT NULL DEFAULT 1 COMMENT '可见性',
  `priority` int NOT NULL COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '咒语书' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spellbook
-- ----------------------------
INSERT INTO `spellbook` VALUES (1, '教室', 'classroom', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (2, '卧室', 'bedroom', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (3, '厨房', 'kitchen', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (4, '图书馆', 'library', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (5, '健身房', 'gym', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (6, '舞台', 'stage', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (7, '村庄', 'village', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:25', 0);
INSERT INTO `spellbook` VALUES (8, '码头', 'pier', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (9, '铁路道口', 'railroad crossing', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (10, '人行道', 'sidewalk', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (11, '花园', 'garden', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (12, '田野', 'field', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (13, '摩天轮', 'ferris wheel', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (14, '温泉', 'onsen', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (15, '餐厅', 'restaurant', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (16, '公交站', 'bus stop', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (17, '体育场', 'stadium', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (18, '钟楼', 'clock tower', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (19, '教堂', 'church', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (20, '水族馆', 'aquarium', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (21, '便利店', 'convenience store', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (22, '废墟', 'ruins', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (23, '城堡', 'castle', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (24, '摩天大楼', 'skyscraper', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (25, '霓虹灯', 'neon lights', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (26, '竹林', 'bamboo forest', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (27, '大自然', 'nature', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (28, '瀑布', 'waterfall', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (29, '湖', 'lake', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (30, '洞穴', 'cave', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (31, '公园', 'park', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:26', 0);
INSERT INTO `spellbook` VALUES (32, '沙漠', 'desert', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:27', 0);
INSERT INTO `spellbook` VALUES (33, '大海', 'ocean', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:27', 0);
INSERT INTO `spellbook` VALUES (34, '花田', 'flower field', '场景', NULL, 1, 1, '2023-08-03 00:09:24', '2023-08-03 00:35:27', 0);
INSERT INTO `spellbook` VALUES (35, '天空', 'sky', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (36, '白天', 'day', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (37, '云', 'cloud', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (38, '夜晚', 'night', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (39, '月亮', 'moon', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (40, '夜空', 'night sky', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (41, '满月', 'full moon', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:32', 0);
INSERT INTO `spellbook` VALUES (42, '星空', 'starry sky', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (43, '流星', 'shooting star', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (44, '下雨', 'rain', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (45, '落日', 'sunset', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (46, '太阳', 'sun', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (47, '月光', 'moonlight', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (48, '天际线', 'skyline', '天气', NULL, 1, 2, '2023-08-03 00:09:24', '2023-08-03 00:35:33', 0);
INSERT INTO `spellbook` VALUES (49, '闪耀效果', 'sparkle', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (50, '背景虚化', 'depth of field', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (51, '逆光', 'backlighting', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (52, '剪影', 'silhouette', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (53, '速度线', 'speed lines', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (54, '胶片质感', 'film grain', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (55, '电影光效', 'cinematic lighting', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (56, '游戏CG', 'game cg', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (57, '像素风', 'pixel art', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (58, '广角镜头', 'wide shot', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (59, '鱼眼镜头', 'fisheye', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (60, '特写', 'close-up', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (61, '油画', 'oil painting', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (62, '铅笔画', 'graphite', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (63, '水彩', 'watercolor', '构图效果', NULL, 1, 3, '2023-08-03 00:09:24', '2023-08-03 00:35:39', 0);
INSERT INTO `spellbook` VALUES (64, '手套', 'gloves', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (65, '长袖', 'long sleeves', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (66, '蝴蝶结', 'bow', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (67, '披风', 'cape', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (68, '外套', 'coat', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (69, '背心', 'vest', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (70, '西装夹克', 'blazer', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (71, 'T恤', 't-shirt', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (72, '手镯', 'bracelet', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (73, '卫衣', 'hoodie', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (74, '裙子', 'skirt', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (75, '短裤', 'shorts', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (76, '七分裤', 'capri pants', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (77, '牛仔裤', 'jeans', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (78, '旗袍', 'cheongsam', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (79, '西装', 'suit', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (80, '运动服', 'gym uniform', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (81, '长靴', 'boots', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (82, '高跟鞋', 'high heels', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (83, '领带', 'necktie', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (84, '项链', 'necklace', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (85, '围巾', 'scarf', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:45', 0);
INSERT INTO `spellbook` VALUES (86, '吊坠', 'pendant', '人物装饰', NULL, 1, 4, '2023-08-03 00:09:24', '2023-08-03 00:35:46', 0);
INSERT INTO `spellbook` VALUES (87, '长发', 'long hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (88, '短发', 'short hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (89, '超短发', 'very short hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (90, '超长发', 'absurdly long hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (91, '刘海', 'bangs', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (92, '双马尾', 'twintails', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (93, '辫子', 'braid', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (94, '呆毛', 'aoge', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (95, '丸子头', 'hair bun', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (96, '长直发', 'long hair, straight hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (97, '长卷发', 'long hair, curly hair', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (98, '双麻花辫', 'dreadlocks', '发型', NULL, 1, 5, '2023-08-03 00:09:24', '2023-08-03 00:36:08', 0);
INSERT INTO `spellbook` VALUES (99, '微笑', 'smile', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (100, '脸红的', 'blush', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (101, '闭上眼睛', 'closed eyes', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (102, '流泪', 'steaming tears', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (103, '惊讶', 'surprised', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (104, '生气', 'angry', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:11', 0);
INSERT INTO `spellbook` VALUES (105, '严肃', 'serious', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (106, '坏笑', 'evil smile', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (107, '得意', 'doyagao', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (108, '兴奋', 'excited', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (109, '失望', 'disappointed', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (110, '嘟嘴', 'homu', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (111, '撅嘴', 'pout', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (112, '吐舌头', 'tongue out', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (113, '虎牙', 'fang', '表情', NULL, 1, 6, '2023-08-03 00:09:24', '2023-08-03 00:36:12', 0);
INSERT INTO `spellbook` VALUES (114, '单人', 'solo', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:19', 0);
INSERT INTO `spellbook` VALUES (115, '1个女人', '1girl', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:19', 0);
INSERT INTO `spellbook` VALUES (116, '1个男人', '1boy', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:19', 0);
INSERT INTO `spellbook` VALUES (117, '2个女人', '2girls', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:19', 0);
INSERT INTO `spellbook` VALUES (118, '2个男人', '2boys', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:19', 0);
INSERT INTO `spellbook` VALUES (119, '全身', 'full body', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (120, '上半身', 'upper body', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (121, '侧脸', 'in profile', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (122, '仰视', 'looking up', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (123, '俯视', 'looking down', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (124, '对视', 'eye contact', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (125, '回眸', 'looking back', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (126, '坐着', 'sitting', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (127, '站着', 'standing', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (128, '躺着', 'lying', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (129, '歪着头', 'head tilt', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (130, '胜利手势', 'v sign', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (131, '牵手', 'holding hands', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (132, '蹲下', 'squatting', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (133, '正坐', 'seiza', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (134, '双手叉腰', 'hands on hips', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (135, '双手插兜', 'hands in pockets', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (136, '招手', 'waving', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (137, '张开双臂', 'spread arms', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (138, '撩头发', 'hair tucking', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (139, '拥抱', 'cuddling', '姿态', NULL, 1, 7, '2023-08-03 00:09:24', '2023-08-03 00:36:20', 0);
INSERT INTO `spellbook` VALUES (140, '椅子', 'chair', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:26', 0);
INSERT INTO `spellbook` VALUES (141, '伞', 'umbrella', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:26', 0);
INSERT INTO `spellbook` VALUES (142, '背包', 'backpack', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:26', 0);
INSERT INTO `spellbook` VALUES (143, '茶杯', 'teacup', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:26', 0);
INSERT INTO `spellbook` VALUES (144, '筷子', 'chopsticks', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (145, '相机', 'camera', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (146, '吉他', 'guitar', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (147, '摩托车', 'motorcycle', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (148, '骑车', 'bicycle', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (149, '魔杖', 'wand', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (150, '花束', 'bouquet', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (151, '红玫瑰', 'red rose', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (152, '樱花', 'cherry blossoms', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (153, '吐司', 'toast', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (154, '汉堡包', 'burger', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (155, '寿司', 'sushi', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (156, '披萨', 'pizza', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (157, '包子', 'baozi', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (158, '三明治', 'sandwich', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (159, '便当', 'bento', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (160, '牛奶', 'milk', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (161, '苹果', 'apple', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (162, '西瓜', 'watermelon', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (163, '蛋糕', 'cake', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (164, '甜甜圈', 'doughnut', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (165, '冰淇淋', 'ice cream', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (166, '啤酒', 'beer', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (167, '咖啡', 'coffee', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:27', 0);
INSERT INTO `spellbook` VALUES (168, '可乐', 'coca-cola', '物品', NULL, 1, 8, '2023-08-03 00:09:24', '2023-08-03 00:36:28', 0);

-- ----------------------------
-- Table structure for sys_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client`;
CREATE TABLE `sys_oauth_client`  (
  `client_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源id列表',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密钥',
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '域',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权方式',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限列表',
  `access_token_validity` int NULL DEFAULT NULL COMMENT '认证令牌时效',
  `refresh_token_validity` int NULL DEFAULT NULL COMMENT '刷新令牌时效',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展信息',
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自动放行',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OAuth2客户端配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oauth_client
-- ----------------------------
INSERT INTO `sys_oauth_client` VALUES ('web-admin', '', '123456', 'all', 'password,refresh_token', NULL, NULL, 3600, 86400, NULL, 'true');
INSERT INTO `sys_oauth_client` VALUES ('wechat', '', '123456', 'all', 'wechat,refresh_token', NULL, NULL, 3600, 604800, NULL, 'true');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `url_perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL权限标识',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色',
  `order_id` bigint NOT NULL COMMENT '排序，从0开始，越小越靠前',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口权限配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (2, '后台管理-客户端管理', '*:/service/admin/sysOauthClient/**', 'ROLE_ROOT', 0);
INSERT INTO `sys_permission` VALUES (3, '后台管理-权限管理', '*:/service/admin/sysPermission/**', 'ROLE_ROOT', 1);
INSERT INTO `sys_permission` VALUES (6, '后台管理-用户管理', '*:/service/admin/user/**', 'ROLE_ROOT', 2);
INSERT INTO `sys_permission` VALUES (7, '后台管理-用户管理-查询用户', 'GET:/service/admin/user/*/*', 'ROLE_ADMIN,ROLE_ROOT', 2);
INSERT INTO `sys_permission` VALUES (21, '小程序-SD-文生图', 'POST:/service/sd/txt2img', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 100);
INSERT INTO `sys_permission` VALUES (22, '小程序-画夹分页查询', 'GET:/service/pic/page', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 101);
INSERT INTO `sys_permission` VALUES (23, '小程序-图片详情查询', 'GET:/service/pic/get', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 101);
INSERT INTO `sys_permission` VALUES (24, '后台管理-模型管理', '*:/service/admin/model/**', 'ROLE_ADMIN,ROLE_ROOT', 3);
INSERT INTO `sys_permission` VALUES (25, '小程序-SD-脸部修复', 'POST:/service/sd/restoreFace', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 100);
INSERT INTO `sys_permission` VALUES (26, '小程序-画夹批量删除', 'POST:/service/pic/removeByIds', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 101);
INSERT INTO `sys_permission` VALUES (27, '小程序-查询当前登录用户信息', 'GET:/service/user/getMyUserInfo', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 102);
INSERT INTO `sys_permission` VALUES (28, '后台管理-任务管理', '*:/service/admin/task/**', 'ROLE_ADMIN,ROLE_ROOT', 4);
INSERT INTO `sys_permission` VALUES (29, '后台管理-枚举查询', '*:/service/admin/enum/**', 'ROLE_ADMIN,ROLE_ROOT', 5);
INSERT INTO `sys_permission` VALUES (30, '小程序-分页查询当前登录用户积分记录', 'GET:/service/user/pagePointRecord', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 102);
INSERT INTO `sys_permission` VALUES (31, '小程序-积分记录操作来源枚举', 'GET:/service/admin/enum/getPointOperationSourceEnum', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 102);
INSERT INTO `sys_permission` VALUES (32, '后台管理-图片管理', '*:/service/admin/pic/**', 'ROLE_ADMIN,ROLE_ROOT', 6);
INSERT INTO `sys_permission` VALUES (33, '后台管理-积分流水', '*:/service/admin/pointRecord/**', 'ROLE_ROOT,ROLE_ADMIN', 7);
INSERT INTO `sys_permission` VALUES (34, '后台管理-投稿审核', '*:/service/admin/picShare/**', 'ROLE_ADMIN,ROLE_ROOT', 8);
INSERT INTO `sys_permission` VALUES (35, '后台管理-QQ机器人', '*:/service/admin/mirai/**', 'ROLE_ADMIN,ROLE_ROOT', 9);
INSERT INTO `sys_permission` VALUES (36, '后台管理-请求日志', '*:/service/admin/requestLog/**', 'ROLE_ADMIN,ROLE_ROOT', 10);
INSERT INTO `sys_permission` VALUES (37, '后台管理-请求限制', '*:/service/admin/requestLimitLog/**', 'ROLE_ADMIN,ROLE_ROOT', 11);
INSERT INTO `sys_permission` VALUES (38, '小程序-SD-超分辨率', 'POST:/service/sd/superResolution', 'ROLE_USER,ROLE_ADMIN,ROLE_ROOT', 100);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` bigint NOT NULL COMMENT '任务id',
  `request_uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求uuid',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型',
  `body` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'SD DTO',
  `status` int NOT NULL COMMENT '任务状态 0未执行 1已执行 2在队列中 3废弃',
  `point` int NOT NULL COMMENT '消耗积分',
  `exec_source` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行源',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status_createtime`(`status` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `openid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序openid',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `role` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` int NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `daily_point` int NOT NULL DEFAULT 0 COMMENT '每日积分',
  `point` int NOT NULL DEFAULT 0 COMMENT '积分',
  `level` int NOT NULL DEFAULT 0 COMMENT '等级',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用1是0否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否删除1是0否',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userid`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 1, NULL, 'root', '管理员root', '{bcrypt}$2a$10$HxIova9kMs0F8bHZ.9VV.OU5xlE/Rtl.WIwNWvgqnrASCOeUH6cXu', 'ROLE_ROOT', 'https://picsum.photos/id/141/200', 1, NULL, 0, 995550, 0, 1, '2023-06-10 02:59:19', '2023-08-27 12:06:18', 0);
INSERT INTO `user` VALUES (2, 2, NULL, 'admin', '管理员admin', '{bcrypt}$2a$10$QWqXyPCF2XbQdZ5gaSBTHOJ6mkC8lHDFpHXg/0ymA8iq9EL85O2MW', 'ROLE_ADMIN', 'https://picsum.photos/id/142/200', 1, NULL, 0, 220, 0, 1, '2023-06-10 03:00:12', '2023-08-27 12:07:11', 0);
INSERT INTO `user` VALUES (3, 3, NULL, 'test', '用户test', '{bcrypt}$2a$10$HxIova9kMs0F8bHZ.9VV.OU5xlE/Rtl.WIwNWvgqnrASCOeUH6cXu', 'ROLE_USER', 'https://picsum.photos/id/143/200', 1, NULL, 0, 995550, 0, 1, '2023-06-10 02:59:19', '2023-08-27 12:07:04', 0);

-- ----------------------------
-- Table structure for user_id_sender
-- ----------------------------
DROP TABLE IF EXISTS `user_id_sender`;
CREATE TABLE `user_id_sender`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户id发号表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.28 : Database - oqoqbobo
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`oqoqbobo` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `oqoqbobo`;

/*Table structure for table `sec_dict_type` */

DROP TABLE IF EXISTS `sec_dict_type`;

CREATE TABLE `sec_dict_type` (
  `type_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '字典类型id',
  `type_name` varchar(100) NOT NULL COMMENT '字典类型名称',
  `type_code` varchar(50) DEFAULT NULL COMMENT '字典类型编码',
  `remarks` varchar(500) DEFAULT NULL COMMENT '类型备注信息',
  `del_flag` tinyint DEFAULT '0' COMMENT '逻辑删除：1删除0否',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=COMPACT COMMENT='字典类型';

/*Data for the table `sec_dict_type` */

insert  into `sec_dict_type`(`type_id`,`type_name`,`type_code`,`remarks`,`del_flag`,`create_time`,`update_time`) values (1,'审核状态','checkStatus','checkStatus1待审核2审核通过3驳回',0,'2020-11-25 16:15:23','2020-11-24 15:32:11'),(2,'订单状态','orderStatus','订单状态：1未完成2已完成',0,'2020-11-25 16:15:23','2020-06-16 16:08:38'),(3,'运输条款','transitClause','运输条款：1第三方2马士基',0,'2020-11-25 16:15:23','2020-06-16 17:28:45'),(4,'指派类型','appointType','指派类型appointType:1完成指派2按时间指派',0,'2020-11-25 16:15:23','2020-06-16 17:29:44'),(5,'异常类型','errorType','1:迟到  2:车检异常  3:人证异常  4-其他',0,'2020-11-25 18:28:09','2020-06-19 18:28:09'),(6,'通用是否状态','commonWhether','1:是,0:否',0,'2020-11-25 11:02:19','2020-06-22 11:02:19'),(7,'异常状态','errStatus','异常状态：0-无异常，1-异常',0,'2020-11-25 16:46:54','2020-06-23 16:46:54'),(8,'性别','sexType','9-未知\n1-男\n2-女',0,'2020-11-25 17:13:19','2020-11-25 17:13:19'),(9,'启动禁用状态','useStatus','启动禁用状态:useStatus 默认0启动1禁用',0,'2020-11-29 17:30:56','2020-06-23 17:30:56'),(10,'发布状态','publishStatus','发布状态字典publishStatus 1-未发布，2-已发布',0,'2020-06-23 16:53:27','2020-06-23 16:53:27'),(11,'岗位','position','1-QA人员，2-安保人员',0,'2020-11-28 15:11:23','2020-11-28 15:11:23'),(12,'准驾车型','drivingType','',0,'2020-06-23 17:32:31','2020-06-23 17:32:31'),(13,'燃料类型','fuelType','1柴油  2汽油',0,'2020-11-27 11:20:49','2020-11-27 11:20:49'),(14,'排放标准','emissionType','1-国六，2-国五，3-国四，4-国三，5-国二',0,'2020-11-26 20:32:28','2020-11-26 20:32:28'),(18,'电子围栏图形','graphType','字典graphType:1-点;2-圆;3多边;4-方形',0,'2020-11-30 14:42:42','2020-11-30 14:42:42'),(19,'运输单状态','transportStatus','运输单状态 1-未预约 2-已预约 3-已取消',0,'2020-11-30 14:42:46','2020-11-30 14:42:42'),(20,'车检状态','vehicleCheckStatus','0-未进行车检，1-车检及格，2-车检不及格',0,'2020-11-30 14:42:49','2020-11-30 14:42:42'),(21,'车检类型','vehicleCheckType','1-入园 2-出园',0,'2020-11-30 14:43:16','2020-11-30 14:42:42'),(22,'通过状态','commonPassStatus','1-通过 2-不通过',0,'2020-11-30 14:56:01','2020-11-30 14:42:42'),(23,'目的地类型','destinationType','1-集装箱堆场,2-港口堆场',0,'2020-11-30 17:09:50','2020-11-30 14:42:42'),(24,'考卷题目类型','subjectType','1-单选题，2-多选题，3-判断题',0,'2020-12-02 11:18:54','2020-12-07 14:34:37'),(25,'预约状态','appointStatus','预约状态，默认1-已保存，2-已预约，3已取消',0,'2020-12-02 11:18:57','2020-12-07 14:34:37'),(26,'通知状态','noticeStatus','1-已通知，0-未通知',0,'2020-12-02 11:19:00','2020-12-07 14:34:37'),(27,'人证核验状态','peopleCheckStatus','0-未核验1-已通过2-未通过',0,'2020-12-02 11:19:04','2020-12-07 14:34:37'),(28,'QA检查状态','qaCheckStatus','0-未进行QA检查，1-QA检查通过，2-QA检查不通过',0,'2020-12-02 11:19:07','2020-12-07 14:34:37'),(29,'入园排队预约类型','vehicleAppointType','1-司机预约（小程序） \n2-后台预约（WEB）',0,'2020-12-04 15:19:34','2020-12-04 15:19:34'),(30,'处理状态','dealStatus','',0,'2020-12-04 18:49:20','2020-12-04 18:49:20'),(31,'及格状态','passStatus','passStatus:0-不及格 1-及格',0,'2020-12-07 14:34:37','2020-12-07 14:34:37'),(32,'进出类型','transferType','进出类型，1-进，2-出',0,'2020-12-07 16:55:54','2020-12-07 16:55:54'),(33,'车辆类型','carType','车辆类型，carType 1-运输任务，2-白名单，3-访客预约',0,'2020-12-07 16:56:16','2020-12-07 16:56:16'),(34,'eventType','eventType','',0,'2020-12-08 20:45:10','2020-12-08 20:45:10'),(35,'司机黑名单生成原因','driverBlackCreateType','',0,'2020-12-11 18:41:22','2020-12-11 18:41:22'),(36,'箱体朝向','orientate','箱体朝向:1箱门朝内、2箱门朝外',0,'2020-12-15 18:30:05','2020-12-15 18:30:05'),(37,'到达确认类型','arrivalAffirmType','到达确认类型arrivalAffirmType:1小程序2平台',0,'2020-12-16 14:37:17','2020-12-16 14:37:17'),(38,'货物运输状态','goodsStatus','货物运输状态goodsStatus：1未完成2已完成',0,'2020-12-16 15:06:57','2020-12-16 15:06:57'),(39,'邮箱发送类型','sendType','',0,'2021-01-29 16:29:24','2021-01-29 16:29:24'),(40,'角色类型','roleType','1-系统角色类型：superAdmin    只有顶级机构\n2-企业角色类型：companyAdmin  只有顶级机构\n3-园区角色类型：parkAdmin    只有顶级机构\n4-承运商角色类型：carrierAdmin  只有顶级机构\n5-员工角色类型：staffAdmin  （多个角色，机构园区）',0,'2021-03-03 14:36:46','2021-03-03 14:36:46'),(41,'订单类型','orderType','1-入库订单;2-出库订单',0,'2021-03-09 14:58:57','2021-03-09 14:58:57'),(42,'订单分配状态','allocationStatus','订单分配状态 0-未分配 1-已分配',0,'2021-03-09 15:05:36','2021-03-09 15:05:36'),(43,'考卷类型','paperType','考卷类型： 1-司机 2-访客 3-员工',0,'2021-03-10 15:09:43','2021-03-10 15:09:43'),(44,'短信发送类型','smsRecordType','短信发送状态：0-失败 1-成功',0,'2021-03-10 16:51:44','2021-03-10 16:51:44'),(45,'学习资料','material','学习资料类型 1-司机 2-访客',0,'2021-03-10 17:32:43','2021-03-10 17:32:43'),(46,'运输类型','transportType','',0,'2021-03-11 15:53:17','2021-03-11 15:53:17'),(47,'省市区类型','areaType','类型areaType： 1省份2城市3区',0,'2021-03-12 09:48:21','2021-03-12 09:48:21'),(48,'访客审核状态','apptStatus','审核状态 1-待审核，2-审核通过，3-驳回',0,'2021-03-15 10:07:12','2021-03-15 10:07:12'),(49,'用户类型','personType','用户类型，1-访客，2-员工',1,'2021-03-16 14:17:01','2021-03-16 14:17:01'),(50,'用户类型','personType','1-访客，2-员工',0,'2021-03-16 16:03:44','2021-03-16 16:03:44'),(51,'yonghu类型','otherType','用户类型，1-访客，2-员工',1,'2021-03-16 18:20:51','2021-03-16 18:20:51'),(52,'访问人员类型','vPersonType','人员类型 1-司机 2-访客',0,'2021-03-17 11:24:00','2021-03-17 11:24:00'),(53,'访客人证通过状态','isCheck','isCheck人证核验是否通过 1-通过，2-不通过',0,'2021-03-17 11:31:17','2021-03-17 11:31:17'),(54,'是否申请人','isApplicant','isApplicant是否申请人 1-是，0-否',0,'2021-03-17 11:32:28','2021-03-17 11:32:28'),(55,'装卸类型','loadingType','装卸类型 loadingType 1-装载 2-卸货',1,'2021-03-18 11:15:40','2021-03-18 11:15:40'),(56,'预约类型','reservationType','reservationType预约类型，引用字典表   1-承运商预约，2-园区短驳',0,'2021-03-18 11:20:11','2021-03-18 11:20:11'),(57,'预约任务异常类型','appointErrorType','异常类型appointErrorType:1:迟到2取消',0,'2021-03-18 14:59:22','2021-03-18 14:59:22'),(58,'调度确认状态','confirmStatus','调度确认状态 0-未确认 1-已确认 2-已取消',0,'2021-03-18 15:00:36','2021-03-18 15:00:36'),(59,'错误状态','errorStatus','0-未处理  1-已处理',0,'2021-03-18 15:03:57','2021-03-18 15:03:57'),(60,'车辆状态','vehicleStatus','车辆状态，引用字典表信息，0-未到达，1-等待入园，2-已入园，3-已离园',0,'2021-03-18 15:06:01','2021-03-18 15:06:01'),(61,'人证核验类型','peopleCheckType','人证核验类型，引用字典表，1-设备核验，2-人工核验',0,'2021-03-18 15:12:33','2021-03-18 15:12:33'),(62,'检查类型','checkObrType','字典checkObrType1人工手动2设备自动',0,'2021-03-18 15:15:43','2021-03-18 15:15:43'),(63,'作业状态','workingStatus','作业状态，引用字典表信息，0-未作业，1-作业中，2-作业预警，3-作业超时，4-作业完成，5-作业超时完成',0,'2021-03-18 15:17:38','2021-03-18 15:17:38'),(64,'是否短驳承运商','shortBarge','是否短驳承运商 0：否 1：是',0,'2021-03-20 16:51:32','2021-03-20 16:51:32'),(65,'安防设备类型','securityType','1-摄像头 2-车闸设备 3-人闸设备 4-人证核验设备',0,'2021-03-25 09:50:07','2021-03-25 09:50:07'),(66,'设备平台类型','devicePlatformType','1-安防设备，2-消防设备',0,'2021-03-25 09:54:14','2021-03-25 09:54:14'),(67,'在线状态','onlineStatus','0-离线 1-在线',0,'2021-03-25 09:55:42','2021-03-25 09:55:42'),(68,'设备类型','deviceType','-主机 2-传输装置 3-剩余电流式电气火灾监控探测器 4-市电探测器 5-测温式电气火灾监控探测器 6-火灾报警控制器 7- 压力传感器 8-液位传感器',0,'2021-03-25 09:59:02','2021-03-25 09:59:02'),(69,'所在仓库区域','warehousePosition','仓库区域名称列表',0,'2021-03-25 10:00:36','2021-03-25 10:00:36'),(70,'模拟量类型','analogValueType','待定',0,'2021-03-25 13:17:36','2021-03-25 13:17:36'),(71,'告警处理状态','alarmStatus','处理状态:0-未处理;1-已处理',0,'2021-03-25 19:01:42','2021-03-25 19:01:42'),(72,'告警类型','alarmType','10-摄像头停车位行人入侵告警;200-火警;201-电流过高;202-电流过低;203-温度过高;204-温度过低;205-电压过高;206-电压过低;207-视频火警;208-烟雾报警;209-水压过高;210-水压过低;211-液位过高;212-液位过低;300-故障',0,'2021-03-25 19:03:11','2021-03-25 19:03:11'),(73,'确认结果','alarmResult','1-报警;2-误报;3-测试',0,'2021-03-25 19:09:00','2021-03-25 19:09:00'),(74,'操作类型','deviceOperationType','1-人工手动\n2-自动',0,'2021-03-30 16:27:04','2021-03-30 16:27:04'),(75,'出园类型','exitParkType','待定',0,'2021-03-31 16:42:43','2021-03-31 16:42:43'),(76,'模板类型','tmplType','1.订单 2.待定',0,'2022-01-11 15:48:14','2022-01-11 15:48:14'),(77,'状态','commonEffectiveStatus','0：无效，1：有效，2：冻结',0,'2022-03-01 12:00:32','2022-03-01 12:00:32'),(78,'机构类型','orgType','1-公司 2-园区 10-仓库 11-垛口',0,'2022-03-11 11:28:31','2022-03-11 11:28:31');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
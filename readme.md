# LightningDataInterface
# 雷电数据接口

本项目使用Spring+Hibernate+Dubbo开发
可使用两种方式调用服务
1. restful方式（下方内容有详细描述）
2. rpc方式（调用接口可参看代码）

## 主要数据接口

### 1 雷电密度
对应Controller “service/adtd/LightningDensity”
#### 参数
| 参数 | 含义 | 取值范围 | 是否必须 | 默认取值 |
| --- | --- | --- | --- | --- |
| year | 年份 | 2008-2016 | 是 | 无 |
| radium | 半径 | 1-20正整数 | 是 | 无 |

#### 返回值说明

![](/readmeImg/density.png)

#### 效果展示
![](/readmeImg/density_show.png)
![](/readmeImg/density_show2.png)
### 2 雷电活动
对应Controller “service/adtd /LightningActiveByTime”
#### 参数
| 参数 | 含义 | 取值范围 | 是否必须 | 默认取值 |
| --- | --- | --- | --- | --- |
| StartTime | 开始时间 | 指定的日期和时间距 1970 年 1 月 1 日午夜（GMT 时间）之间的毫秒数 | 是 | 无 |
| EndTime | 结束时间 | 指定的日期和时间距 1970 年 1 月 1 日午夜（GMT 时间）之间的毫秒数 | 是 | 无 |

#### 返回值说明

| 名称 | 说明 |
| --- | --- |
| latitude | 雷电纬度 |
| longitude | 雷电经度 |
| intensity | 幅值（强度） |
| datetime | 雷电时间 |
#### 效果展示
![][//readmeImg/active.png)
### 3 按区域统计分析
对应Controller “service/adtd / AreaStatistic”
#### 参数
| 参数 | 含义 | 取值范围 | 是否必须 | 默认取值 |
| --- | --- | --- | --- | --- |
| AreaIndex | 区域索引 | 0-40 | 是 | 无 |
| Date | 查询的日期类型 | YYYY<br>YYYY-MM<br>YYYY-MM-dd | 是 | 无 |
#### 返回值说明

xxxx型返回值

![](/readmeImg/xxxx.png)

xxxx-xx型返回值

![](/readmeImg/xxxx-xx.png)

xxxx-xx-xx型返回值

![](/readmeImg/xxxx-xx-xx.png)

#### 效果展示
![](/readmeImg/xxxx-xxShow.png)
![](/readmeImg/xxxx-xxShow2.png)
### 4 按范围统计分析
对应Controller “service/adtd / CricleStatistic”和“service/adtd /RectangleStatistic”
#### 参数
CricleStatistic

| 参数 | 含义 | 取值范围 | 是否必须 | 默认取值 |
| --- | --- | --- | --- | --- |
| Latitude | 纬度 | 28.166-32.207 | 是 | 无 |
| Longitude | 经度 | 105.296-110.206 | 是 | 无 |
| Radium | 半径（公里） | ≤5 | 是 | 无 |
| Date | 查询日期类型 | xxxxxxxx-xxxxxx-xx-xx | 是 | 无 |

Date取值代表含义说明：(&quot;x&quot;表示具体的日期数字)

xxxx代表查询一年统计数据

xxxx-xx代表查询一个月统计数据

xxxx-xx-xx代表查询一天统计数据

RectangleStatistic

| 参数 | 含义 | 取值范围 | 是否必须 | 默认取值 |
| --- | --- | --- | --- | --- |
| latitudeLower | 左下纬度 | 28.166-32.207 | 是 | 无 |
| longitudeLeft | 左下经度 | 105.296-110.206 | 是 | 无 |
| latitudeUpper | 右上纬度 | 28.166-32.207 | 是 | 无 |
| longitudeRight | 右上经度 | 105.296-110.206 | 是 | 无 |
| Date | 查询日期类型 | xxxxxxxx-xxxxxx-xx-xx | 是 | 无 |

Date取值代表含义说明：(&quot;x&quot;表示具体的日期数字)

xxxx代表查询一年统计数据

xxxx-xx代表查询一个月统计数据

xxxx-xx-xx代表查询一天统计数据

矩形的面积不能超过25平方公里

#### 返回值说明
CricleStatistic xxxx型返回值

![](/readmeImg/CricleStatistic-xxxx.png)

CricleStatistic xxxx-xx型返回值

![](/readmeImg/CricleStatistic-xxxx-xx.png)

CricleStatistic xxxx-xx-xx型返回值（其中的时间段为1小时）

![](/readmeImg/CricleStatistic-xxxx-xx-xx.png)

RectangleStatistic xxxx型返回值

![](/readmeImg/RectangleStatisticxxxx.png)

RectangleStatistic xxxx-xx型返回值

![](/readmeImg/RectangleStatisticxxxx-xx.png)

RectangleStatistic xxxx-xx-xx型返回值（其中的时间段为1小时）

![](/readmeImg/RectangleStatisticxxxx-xx-xx.png)
#### 效果展示
![](/readmeImg/AreaStatistic1.png)
![](/readmeImg/AreaStatistic2.png)
![](/readmeImg/AreaStatistic3.png)
# 静态图片的切图与展示
## java服务端用作静态图片的切片
* 切片标准（与常用的瓦片地图一致）：https://msdn.microsoft.com/en-us/library/bb259689.aspx
* 本地保存切片或上传至OSS
## js前端通过openlayers进行展示切片。
* 绘制不同形状并标记，记录与之对应的信息
* WEBGL对切图进行颜色渲染（透明度，灰度，亮度）
* 控制区域的边界（缩放，拖拽，显示）
* 截图当前显示区域并上传

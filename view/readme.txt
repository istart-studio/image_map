-----服务端启动
java -jar xxx.jar
-----
1.请求图片
1.1.进入http://127.0.0.1:19797/doc.html
1.2.点击进入：/source/tiles/handleImage 中的"在线调试"标签
1.3.输入两个参数，提交后，服务端后台切图
localFilePath:/Users/dongyan/Downloads/source/thumbnail4.jpg
baseDir:/Users/dongyan/Downloads/source/tiles
eg: http://127.0.0.1:19797/source/tiles/handleImage?localFilePath=%2FUsers%2Fdongyan%2FDownloads%2Foriginal%2F4_6827_13793.jpg&baseDir=%2FUsers%2Fdongyan%2FDownloads%2Ftiles
1.4.点击"发送"
1.5.等待切片完成，提示"zoom level 1 cut done!"
查看对应的瓦片本地文件夹是否存在为1的文件夹

2.chrome打开index.html
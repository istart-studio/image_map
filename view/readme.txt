1.请求图片
进入http://127.0.0.1:19797/doc.html
输入两个参数，提交后，服务端后台切图
localFilePath:/Users/dongyan/Downloads/original/4_6827_13793.jpg
baseDir:/Users/dongyan/Downloads/tiles
eg: http://127.0.0.1:19797/source/tiles/handleImage?localFilePath=%2FUsers%2Fdongyan%2FDownloads%2Foriginal%2F4_6827_13793.jpg&baseDir=%2FUsers%2Fdongyan%2FDownloads%2Ftiles
2.等待图片切完
查看对应的瓦片本地文件夹是否存在为1的文件夹
3.请求index.html
# HotFix_SDK2
针对sdk的热修复方案

一、工程jar代码注入：   
1 将sdk工程的依赖的第三方包添加到patch/inject/libs文件夹下(包括android.jar以及gradle依赖的三方库)  
2 运行patch/inject文件夹下 ./addlib.sh   
3 运行patch/inject文件夹下 ./inject.sh xxx.jar   
二、制作补丁  
1 将原始发现bug的jar包放入patch/oldjar文件夹下  
2 将修复好bug的jar包放入patch/newjar文件夹下  
3 运行脚本patch.sh 1.0  
4 最终生成的补丁在patch/patch文件夹下  
注意：
 要运行本工程，首先需要修改local.properties下sdk.home和sdk.aapt为当前运行环境的配置
 另外不要删除项目中的任何文件夹(一定保证patch/patch文件夹存在，一定保证mylibrary工程下assets目录存在)
  需要将补丁文件定义成patch.jar，然后放在手机根目录才能生效(mylibray工程/PAHot.java里有定义)

详细介绍说明：http://blog.csdn.net/killer991684069/article/details/51218428

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

详细介绍说明：http://blog.csdn.net/killer991684069/article/details/51218428

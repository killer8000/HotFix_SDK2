#!/usr/bin/env bash
JAR_PATH=$1
OUT_PATH=$2
EX_CFG=$3
DIR=out_jar

if [ -z "$1" ];then
 echo -e "\033[31;49;1m-------------------"
 echo "|请输入待注入jar路经|"
 echo "-------------------"
 echo  -e " \033[39;49;0m"
exit
fi

if [ -z "$2" ];then
 echo -e "\033[31;49;1m-------------------"
 echo "|请输入目标路径|"
 echo "-------------------"
 echo  -e " \033[39;49;0m"
exit
fi

mkdir -m 777 $OUT_PATH/$DIR
#参数1 表示待注入的jar（必填） 参数二 表示目标文件夹（必填） 参数三表示排除哪些类不被注入（选填 ，如果需要则必须是全类名，有混淆就是混淆后的全类名）
java -jar inject.jar ${JAR_PATH} ${OUT_PATH}/$DIR ${EX_CFG}


cp -r META-INF $OUT_PATH/$DIR

cd ${OUT_PATH}/$DIR

jar -cvfM ${JAR_PATH##*/} .

for file in `find .`
do
[[ $file =~ ${JAR_PATH##*/} ]] || rm -r $file
done
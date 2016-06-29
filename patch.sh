#!/bin/bash
FLAG=false
if [ -z "$1" ];then
 echo -e "\033[31;49;1m-------------------"
 echo "|请输入补丁版本号|"
 echo "-------------------"
 echo  -e " \033[39;49;0m"
exit
else
PATCH_VERSION=$1
fi


#get the class's md5 one by one in oldjar
cd patch/oldjar
 echo -n "" > oldjarinfo.txt
 #make sure there is only one jar file in oldjar directory
if [ `ls *.jar|wc -l` = "1" ];then
echo ""
else
 echo -e "\033[31;49;1m-----------------------"
 echo "|oldjar文件夹下不止一个jar|"
 echo "-----------------------"
 echo  -e " \033[39;49;0m"
exit
fi

for file in `find . `
do
#unzip jar file
[[ $file =~ ".jar" ]] && echo "$file" && jar -xvf $file
done

for sdkpatch in `find . `
do
# get class's md5
[[ $sdkpatch =~ ".class" ]] && md5=`md5 $sdkpatch` && md5=${md5:0-32}  && echo "$sdkpatch=$md5" >> oldjarinfo.txt

done
if [ -s oldjarinfo.txt ]; then
echo ""
else
 echo -e "\033[31;49;1m-----------------------"
 echo "|似乎没有导入有bug的对比jar包|"
 echo "-----------------------"
 echo  -e " \033[39;49;0m"
exit
fi


#get the class's md5 one by one in newjar
cd ../newjar
echo -n "" > newjarinfo.txt
 #make sure there is only one jar file in newjar directory
 if [ `ls *.jar|wc -l` = "1" ];then
 echo ""
 else
  echo -e "\033[31;49;1m-----------------------"
  echo "|newjar文件夹下不止一个jar|"
  echo "-----------------------"
  echo  -e " \033[39;49;0m"
 exit
 fi
for file in `find . `
do
[[ $file =~ ".jar" ]] && NEW_JAR_NAME=$file && jar -xvf $file
done

#check sdkversion
#[[ $NEW_JAR_NAME =~ $1 ]] && FLAG=true
#if [ "$FLAG" != "true" ];then
# echo -e "\033[31;49;1m-----------------------------------------"
#echo "sdk版本不匹配，请核对 "
# echo "sdk版本=${NEW_JAR_NAME}"
# echo "需要制作补丁的版本=$1"
# echo "-----------------------------------------"
# echo  -e " \033[39;49;0m"
# exit
#fi

for sdkpatch in `find . `
do

[[ $sdkpatch =~ ".class" ]] && md5=`md5 $sdkpatch` && md5=${md5:0-32}  && echo "$sdkpatch=$md5" >> newjarinfo.txt

done

if [ -s newjarinfo.txt ]; then
echo ""
else
echo -e "\033[31;49;1m-------------------------"
 echo "|似乎没有导入更新bug后的jar包|"
 echo "-------------------------"
 echo  -e " \033[39;49;0m"
exit
fi


#get the class‘name that is modified
diff newjarinfo.txt ../oldjar/oldjarinfo.txt >> diff.txt

if [ -s diff.txt ];then
echo ""
 else
 echo -e "\033[31;49;1m---------------------------"
 echo "|预期的jar包没有内容更新,请确认|"
 echo "---------------------------"
 echo  -e " \033[39;49;0m"
 exit
fi

CURTIME=$((`date +%y%m%d%H%M%S`))
mkdir -m 777 ../patch/patch_$CURTIME

mv diff.txt ../patch/patch_$CURTIME

mkdir -m 777 ../patch/patch_$CURTIME/patch
mkdir -m 777 ../patch/patch_$CURTIME/patch/temp
# find the class who's name in diff.txt
for sdkpatch in `find . `
do

[[ $sdkpatch =~ ".class" ]] && grep $sdkpatch ../patch/patch_$CURTIME/diff.txt && cp $sdkpatch ../patch/patch_$CURTIME/patch/temp

done
cd ../patch/patch_$CURTIME/patch


#create jar file with the modified classes found in newjar
jar cvfM patch.jar .
rm -r temp
cp -r ../../../META-INF ../patch
cd ../../../dex2jar-2.0


#create dexfile
./d2j-jar2dex.sh ../patch/patch_$CURTIME/patch/patch.jar
for dexfile in `find . `
do

[[ $dexfile =~ ".dex" ]] && mv $dexfile ../patch/patch_$CURTIME/patch/classes.dex

done
cd ../patch/patch_$CURTIME/patch
rm  patch.jar

#verify the dex
if [ -f classes.dex ];then
echo ""
else
echo -e "\033[31;49;1m---------------------"
 echo "|dex文件生成失败|"
 echo "---------------------"
 echo  -e " \033[39;49;0m"
exit
fi

DEFILE_NAME=patch_${PATCH_VERSION}_${CURTIME}


#create patch file with end name ".jar"
jar -cvfM $DEFILE_NAME.jar .

#get patch's md5
jarMD5=`md5 $DEFILE_NAME.jar`
jarMD5=${jarMD5:0-32}


#write the patch's info to file 
echo "MD5=$jarMD5" > ./${DEFILE_NAME%}.mf
echo "PATCH_VERSION=$PATCH_VERSION" >> ./${DEFILE_NAME}.mf
echo "PATCH_NAME=$DEFILE_NAME.jar" >> ./${DEFILE_NAME}.mf
#zip the patch file
zip -r ${DEFILE_NAME}.zip ${DEFILE_NAME}.jar

rm $DEFILE_NAME.jar
rm classes.dex
rm -r META-INF
echo -e "\033[32;49;1m"
echo "----------------------"
echo "| build patch sucess |"
echo "----------------------"
echo -e "\033[39;49;0m"

#!/usr/bin/env bash
#SOURCE_PATH=mylibrary/src/main/java
#M_PATH=mylibrary/src/main/AndroidManifest.xml
#S_PATH=mylibrary/src/main/res
#A_PATH=mylibrary/src/main/assets
#I_PATH=/Applications/AS/dv/android-sdk/platforms/android-23/android.jar
#AAPT_PATH=/Applications/AS/dv/android-sdk/build-tools/23.0.2/aapt
#$AAPT_PATH package -f -m -J mylibrary/gen -M $M_PATH -S $S_PATH -I $I_PATH -F gen/mm.jar
##$AAPT_PATH package -f -m -J gen -M $M_PACH -S $S_PATH -I $I_PATH
#javac - $SOURCE_PATH -classpath mylibrary/gen -d mylibrary/build_newsourcepath
echo "$(pwd)"
#打包lib 将资源文件拷贝到app的assets目录下
./gradlew package_src_jar
cp -r mylibrary/gen/mm.jar app/src/main/assets
cd patch/inject
#将lib工程生成的jar包进行注入，以备后续热修复使用
java -jar inject.jar ../../mylibrary/out/lib/classes.jar mylibrary/out
#将注入后的jar包拷贝到app的libs下
cd ../..
cp -r mylibrary/out/lib/classes.jar app/libs
#将热修复的jar包拷贝到app的libs下
cp -r mylibrary/libs/hotfix.jar app/libs

#./gradlew build --stacktrace

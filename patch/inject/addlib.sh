#!/usr/bin/env bash


CLASSPATH=Class-Path:
cd libs
for file in `find . `
do
if [ "${file##*/}" = "." ]; then
echo "${file##*/}"
else
CLASSPATH=${CLASSPATH}\ libs/`echo "${file##*/}"`
fi
done
echo ${CLASSPATH}

mkdir -m 777 ../temp
cd ../temp
jar -xvf ../inject.jar

echo "" > ../temp/META-INF/MANIFEST.MF
echo "Manifest-Version: 1.0" > ../temp/META-INF/MANIFEST.MF
echo $CLASSPATH >> ../temp/META-INF/MANIFEST.MF
echo "Main-Class: patch.inject" >> ../temp/META-INF/MANIFEST.MF
jar -cvfM inject.jar .

mv inject.jar ../
cd ..
rm -r temp
#!/usr/bin/env bash

set -e
#set -x

GRAAL=~/soft/graalvm-ce-java11-20.1.0
#GRAAL=~/soft/graalvm-ce-java11-20.0.0
UPX=~/soft/upx-3.95-amd64_linux

graal_version=$($GRAAL/bin/java -version 2>&1 | grep '64' | grep GraalVM | sed -E 's#.+(GraalVM.+) \(.+#\1#')
java_version=$($GRAAL/bin/java -version 2>&1 | head -n 1 | sed 's/ version//' | sed 's/"//g')

echo "Java version: $java_version"
echo "Graal version: $graal_version"

BASE_PACKAGE=com.github.jsqry.cli
MAIN_CLASS=App
EXECUTABLE=jsqry

export JAVA_HOME=$GRAAL

mvn clean compile

CP=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
app_version=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args='${project.version}')

tmp_src_dir=/tmp/$EXECUTABLE-tmp

rm -rf $tmp_src_dir

BASE_PACKAGE_PATH=${BASE_PACKAGE//./\/}

mkdir -p $tmp_src_dir/$BASE_PACKAGE_PATH

cat src/main/java/$BASE_PACKAGE_PATH/Constants.java | \
    sed "s/%GRAAL_VERSION%/$graal_version/" | \
    sed "s/%JAVA_VERSION%/$java_version/" | \
    sed "s/%APP_VERSION%/$app_version/" > $tmp_src_dir/$BASE_PACKAGE_PATH/Constants.java

$GRAAL/bin/javac \
    -encoding UTF-8 \
    -d target/classes \
    $tmp_src_dir/$BASE_PACKAGE_PATH/Constants.java

if [[ ! -d ./build ]]
then
    mkdir ./build
fi

cd ./build/

echo
echo Graal
echo

if [[ ! -f $GRAAL/bin/native-image ]]
then
    echo "Installing native-image..."
    bash -c "cd $GRAAL/bin/ ; ./gu install native-image"
fi

$GRAAL/bin/native-image \
    --no-fallback \
    --language:js \
    -H:IncludeResources='.*js$' \
    -H:Log=registerResource: \
    -cp "$CP" $BASE_PACKAGE.$MAIN_CLASS \
    $EXECUTABLE

ls -lh ./$EXECUTABLE

#echo
#echo Upx
#echo
#

#mv ./$EXECUTABLE ./${EXECUTABLE}_orig
#$UPX/upx ./${EXECUTABLE}_orig -o$EXECUTABLE
#ls -lh ./$EXECUTABLE



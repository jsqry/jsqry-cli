#!/usr/bin/env bash

set -e
#set -x

GRAAL=~/soft/graalvm-ce-java11-20.1.0
UPX=~/soft/upx-3.95-amd64_linux

graal_version=$($GRAAL/bin/java -version 2>&1 | grep '64' | grep GraalVM | sed -E 's#.+(GraalVM.+) \(.+#\1#')
java_version=$($GRAAL/bin/java -version 2>&1 | head -n 1 | sed 's/ version//' | sed 's/"//g')

echo "Java version: $java_version"
echo "Graal version: $graal_version"


JAVA="$GRAAL/bin/java"

CP=$(mvn -q exec:exec -Dexec.executable=echo -Dexec.args="%classpath")
echo $CP

echo '[{"a":1},{"a":2}]' | $JAVA -cp "$CP" com.github.jsqry.cli.App "a"
echo '[{"a":1},{"a":2}]' | $JAVA -cp "$CP" com.github.jsqry.cli.App -1 "a[1]"
echo 'wrong' | $JAVA -cp "$CP" com.github.jsqry.cli.App "a"
echo '[]' | $JAVA -cp "$CP" com.github.jsqry.cli.App "]"
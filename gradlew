#!/bin/sh
APP_HOME="$(cd "$(dirname "$0")" && pwd -P)"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Generating gradle wrapper..."
    if command -v gradle >/dev/null 2>&1; then
        gradle wrapper --gradle-version 8.8
    else
        echo "ERROR: gradle-wrapper.jar not found. Install gradle first."
        exit 1
    fi
fi
JAVACMD="${JAVA_HOME:+$JAVA_HOME/bin/}java"
exec "$JAVACMD" -Xmx64m -Xms64m \
    -Dorg.gradle.appname="$(basename "$0")" \
    -classpath "$WRAPPER_JAR" \
    org.gradle.wrapper.GradleWrapperMain "$@"

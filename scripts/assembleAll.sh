set -e
set -x

echo "compile callreco libs ..."
./gradlew clean assemble
echo "libs compiled"

ANDROID_DEMO_LIB_DIR=demo/android/app/libs
rm -rf ${ANDROID_DEMO_LIB_DIR}
mkdir ${ANDROID_DEMO_LIB_DIR}
cp callreco-android-plugin/build/libs/* ${ANDROID_DEMO_LIB_DIR}
cp callreco-core/build/libs/* ${ANDROID_DEMO_LIB_DIR}
cp callreco-agent/build/libs/* ${ANDROID_DEMO_LIB_DIR}
cp callreco-commons/build/libs/* ${ANDROID_DEMO_LIB_DIR}

echo "compile android demo ..."
./demo/android/gradlew clean assemble --stacktrace
echo "android demo compiled"

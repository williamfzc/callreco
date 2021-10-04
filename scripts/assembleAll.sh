set -e
set -x

./gradlew clean assemble

rm -rf demo/android/app/libs/*
cp callreco-android-plugin/build/libs/* demo/android/app/libs
cp callreco-core/build/libs/* demo/android/app/libs

#./demo/android/gradlew clean assemble --stacktrace

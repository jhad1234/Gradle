@echo off

if not exist gradle\wrapper\gradle-wrapper.jar (
  echo ERROR: gradle-wrapper.jar missing
  exit /b 1
)

java -jar gradle\wrapper\gradle-wrapper.jar %*

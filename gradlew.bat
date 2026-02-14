@if "%DEBUG%"=="" @echo off
set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%WRAPPER_JAR%" (
    where gradle >nul 2>nul
    if %ERRORLEVEL% equ 0 ( gradle wrapper --gradle-version 8.8 ) else (
        echo ERROR: gradle-wrapper.jar not found
        exit /b 1
    )
)
set JAVA_EXE=java.exe
if defined JAVA_HOME set JAVA_EXE=%JAVA_HOME%\bin\java.exe
"%JAVA_EXE%" -Xmx64m -Xms64m -Dorg.gradle.appname=%~n0 -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*

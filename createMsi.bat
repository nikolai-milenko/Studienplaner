@echo off
chcp 65001

echo ============================================
echo Studienplaner MSI Builder Script
echo ============================================

echo.
echo [1/3] Alte Runtime löschen...
rmdir /S /Q runtime > nul 2>&1

echo.
echo [2/3] Erstelle benutzerdefinierte Runtime...
"%JAVA_HOME%\bin\jlink" ^
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,jdk.crypto.ec,java.instrument,java.management,java.naming,java.desktop,java.security.jgss,java.sql ^
    --output runtime ^
    --strip-debug ^
    --compress=2 ^
    --no-header-files ^
    --no-man-pages

echo.
echo [3/3] Baue MSI-Installer...

"%JAVA_HOME%\bin\jpackage" ^
    --type msi ^
    --dest . ^
    --input target ^
    --name Studienplaner ^
    --main-jar Studienplaner-0.0.1-SNAPSHOT.jar ^
    --main-class org.springframework.boot.loader.launch.JarLauncher ^
    --runtime-image runtime ^
    --icon icon.ico ^
    --app-version 2.1 ^
    --vendor "Nikolai Milenko" ^
    --win-dir-chooser ^
    --win-shortcut ^
    --win-menu ^
    --win-menu-group "Studienplaner" ^
    --java-options "--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/sun.net.www.protocol.jar=ALL-UNNAMED --add-opens java.base/sun.net.www.protocol.file=ALL-UNNAMED -Dembedded.pg.enabled=true"

echo.
echo ============================================
echo Build abgeschlossen!
echo ============================================

pause

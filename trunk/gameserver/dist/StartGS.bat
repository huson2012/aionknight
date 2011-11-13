@echo off
title [ GS Console ]

:start
java -Xms512m -Xmx1536m -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*;ak-server.jar gameserver.GameServer

SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
if ERRORLEVEL 0 goto end

:restart
echo.
echo Administrator Restart ...
echo.
goto start

:error
echo.
echo Server terminated abnormaly ...
echo.
goto end

:end
echo.
echo Server terminated ...
echo.
pause
@echo off
title [ CServer ]

java -Xms8m -Xmx32m -ea -cp ./libs/*;ak-chat.jar chatserver.ChatServer

SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 1 goto error
goto end

:error
echo.
echo Chat Server Terminated Abnormaly, Please Verify Your Files
echo.

:end
echo.
echo Chat Server Terminated
echo.
pause
@echo off
title [ LS Console ]

:start
java -Xms8m -Xmx32m -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*;ak-login.jar loginserver.LoginServer

SET CLASSPATH=%OLDCLASSPATH%


if ERRORLEVEL 1 goto error
goto end

:error
echo.
echo Login Server Terminated Abnormaly, Please Verify Your Files
echo.

:end
echo.
echo Login Server Terminated
echo.
pause

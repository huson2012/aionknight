@echo off
title [ Aion-Knight Dev. Team ]

:MENU
color FC
CLS
ECHO.
ECHO        =[ RC1 ]===================================================[2.5]=
ECHO.
ECHO               Компилятор исходных кодов The Aion-Knight Dev. Team
ECHO                             (http://aion-knight.ru)
ECHO.
ECHO        =========================[ Главное меню ]========================
ECHO.
ECHO                  [1] - Компилировать сборку целиком
ECHO.
ECHO                  [2] - Компилировать только сервер авторизации
ECHO                  [3] - Компилировать только игровой сервер
ECHO                  [4] - Компилировать только чат-сервер
ECHO                  [5] - Компилировать только библиотеки и классы
ECHO.
ECHO                  [6] - Выход
ECHO.
ECHO        =================================================================
ECHO.
SET /P Ares=              Введите: 1, 2, 3, 4, 5 или 6, затем нажмите ENTER: 
IF %Ares%==1 GOTO FULL
IF %Ares%==2 GOTO LoginServer
IF %Ares%==3 GOTO GameServer
IF %Ares%==4 GOTO ChatServer
IF %Ares%==5 GOTO Commons
IF %Ares%==6 GOTO QUIT

:FULL
CLS
ECHO ============================================================================
ECHO Выбран режим компиляции: Компилировать сборку целиком
ECHO ============================================================================
ECHO.
cd ..\Commons
call ..\Tools\Ant\bin\ant clean dist

cd ..\GameServer
call ..\Tools\Ant\bin\ant clean dist

cd ..\LoginServer
call ..\Tools\Ant\bin\ant clean dist

cd ..\ChatServer
call ..\Tools\Ant\bin\ant clean dist

GOTO :QUIT

:Commons
CLS
ECHO ============================================================================
ECHO Выбран режим компиляции: Только библиотеки и классы
ECHO ============================================================================
ECHO.
cd ..\Commons 
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:GameServer
CLS
ECHO ============================================================================
ECHO Выбран режим компиляции: Только игровой сервер
ECHO ============================================================================
ECHO.
cd ..\GameServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:LoginServer
CLS
ECHO ============================================================================
ECHO Выбран режим компиляции: Только сервер авторизации
ECHO ============================================================================
ECHO.
cd ..\LoginServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:ChatServer
CLS
ECHO ============================================================================
ECHO Выбран режим компиляции: Только чат-сервер
ECHO ============================================================================
ECHO.
cd ..\ChatServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
CLS
GOTO :QUIT

:QUIT
exit
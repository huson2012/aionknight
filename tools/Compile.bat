@echo off
title [ Aion-Knight Dev. Team ]

:MENU
color FC
CLS
ECHO.
ECHO        =[ RC1 ]===================================================[2.5]=
ECHO.
ECHO               ��������� ��室��� ����� The Aion-Knight Dev. Team
ECHO                             (http://aion-knight.ru)
ECHO.
ECHO        =========================[ ������� ���� ]========================
ECHO.
ECHO                  [1] - �������஢��� ᡮ�� 楫����
ECHO.
ECHO                  [2] - �������஢��� ⮫쪮 �ࢥ� ���ਧ�樨
ECHO                  [3] - �������஢��� ⮫쪮 ��஢�� �ࢥ�
ECHO                  [4] - �������஢��� ⮫쪮 ��-�ࢥ�
ECHO                  [5] - �������஢��� ⮫쪮 ������⥪� � ������
ECHO.
ECHO                  [6] - ��室
ECHO.
ECHO        =================================================================
ECHO.
SET /P Ares=              ������: 1, 2, 3, 4, 5 ��� 6, ��⥬ ������ ENTER: 
IF %Ares%==1 GOTO FULL
IF %Ares%==2 GOTO LoginServer
IF %Ares%==3 GOTO GameServer
IF %Ares%==4 GOTO ChatServer
IF %Ares%==5 GOTO Commons
IF %Ares%==6 GOTO QUIT

:FULL
CLS
ECHO ============================================================================
ECHO ��࠭ ०�� �������樨: �������஢��� ᡮ�� 楫����
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
ECHO ��࠭ ०�� �������樨: ���쪮 ������⥪� � ������
ECHO ============================================================================
ECHO.
cd ..\Commons 
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:GameServer
CLS
ECHO ============================================================================
ECHO ��࠭ ०�� �������樨: ���쪮 ��஢�� �ࢥ�
ECHO ============================================================================
ECHO.
cd ..\GameServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:LoginServer
CLS
ECHO ============================================================================
ECHO ��࠭ ०�� �������樨: ���쪮 �ࢥ� ���ਧ�樨
ECHO ============================================================================
ECHO.
cd ..\LoginServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:ChatServer
CLS
ECHO ============================================================================
ECHO ��࠭ ०�� �������樨: ���쪮 ��-�ࢥ�
ECHO ============================================================================
ECHO.
cd ..\ChatServer
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
CLS
GOTO :QUIT

:QUIT
exit
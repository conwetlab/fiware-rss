@echo off

:MENU
cls
echo          =====================
echo          � LICENSE  �
echo          =====================
echo          �  0. Quit          �
echo          �  1. Check         �
echo          �  2. Format        �
echo          �  3. Remove        �
echo          =====================
set /p userinp=License Command?"(0-3):
set userinp=%userinp:~0,1%

if "%userinp%"=="0" goto END
if "%userinp%"=="1" goto check
if "%userinp%"=="2" goto format
if "%userinp%"=="3" goto remove
goto MENU

:check
mvn license:check -Drun-license=true -P license
goto MENU

:format
mvn license:format -Drun-license=true -P license
goto MENU

:remove
mvn license:remove -Drun-license=true -P license
goto MENU

:END
cls
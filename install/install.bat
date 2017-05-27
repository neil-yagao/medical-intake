REM * before run this please make sure mongodb is install correctly
REM * set up directory structure
set BASE_DIR=D:\common-test
if not exist "%BASE_DIR%" mkdir "%BASE_DIR%"
if not exist "%BASE_DIR%\log" mkdir "%BASE_DIR%\log"
if not exist "%BASE_DIR%\img" mkdir "%BASE_DIR%\img"
if not exist "%BASE_DIR%\img\head" mkdir "%BASE_DIR%\img\head"
if not exist "%BASE_DIR%\img\prison" mkdir "%BASE_DIR%\img\prison"
if not exist "%BASE_DIR%\img\police" mkdir "%BASE_DIR%\img\police"
if not exist "%BASE_DIR%\img\medical" mkdir "%BASE_DIR%\img\medical"
if not exist "%BASE_DIR%\video" mkdir "%BASE_DIR%\video"

REM * set up directory structure end

set CREATE_MONGO="on"
if %CREATE_MONGO% == "on" (
	mongo mongo-medical.js
)

REM * since the spring not accept single slash
REM * do remember to change file.location after change base dir variable
start java -Dfile.location="D:/common-test/" -jar medical.jar
set FINGER_DATABASE="on"
if %FINGER_DATABASE% == "on" (
	start java -Dfile.location="D:/common-test/" -jar finger.jar
)

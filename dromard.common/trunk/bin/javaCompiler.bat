@echo off
SETLOCAL

CALL serverenv.bat

cd ..\sources

SET CLASSPATH=%CLASSPATH%;.
SET CLASSPATH=%CLASSPATH%;%WLCLASSPATH%
SET ROOT_PACKAGE=.

echo Compiling ...
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/db/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/html/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/io/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/net/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/net/tags/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/net/url/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/util/*.java

%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/db/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/net/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/net/bean/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/net/servlets/*.java
%JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/app/sourcing/net/tags/*.java

rem %JAVA_HOME%\bin\javac -classpath %CLASSPATH% %ROOT_PACKAGE%/*.java


REM MAKEJAR
echo Archiving ...
%JAVA_HOME%\bin\jar cvf %SERVER_NAME%.jar *

REM DISTRIBUTE
echo Distribuing ...
move %SERVER_NAME%.jar "%SERVER_HOME%\lib\%SERVER_NAME%.jar"
copy "%SERVER_HOME%\lib\%SERVER_NAME%.jar" "%SERVER_HOME%\webserver\WEB-INF\lib\%SERVER_NAME%.jar"

REM CLEAN

ENDLOCAL

pause
exit
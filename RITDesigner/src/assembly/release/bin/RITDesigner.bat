@ECHO OFF
set CLASSPATH=..\lib\*
:: set CLASSPATH=%CLASSPATH%;path/to/needed/jars/my.jar
set path=%PATH%;%JAVA_HOME%
java -cp %CLASSPATH% -Xms128m -Xmx384m com.ricardojlrufino.ritdevx.designer.RIDesignerMain
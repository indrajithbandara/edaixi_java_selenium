@echo OFF
set "_BASE=%~pd0%"
set "TEST_HOME=%_BASE%.."
java -cp "%TEST_HOME%\target\classes;%TEST_HOME%\lib\*" com.ibm.docs.test.common.JUnitRunner %*
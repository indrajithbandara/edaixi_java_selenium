@echo
taskkill /F /IM IEDriverServer.exe /IM chromedriver.exe
for /f %%a in ('dir %TEMP%\scoped_dir* /a:d /b') do rmdir /s /q %TEMP%\%%a
for /f %%a in ('dir %TEMP%\anonymous*webdriver-profile /a:d /b') do rmdir /s /q %TEMP%\%%a
for /f %%a in ('dir %TEMP%\SafariBackups*webdriver /a:d /b') do rmdir /s /q %TEMP%\%%a
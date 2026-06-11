@echo off
if exist "app\src\main\java\model\ProductivitySource.kt" del "app\src\main\java\model\ProductivitySource.kt"
if exist "app\src\main\java\model" rmdir "app\src\main\java\model" 2>nul
echo Selesai membersihkan file duplikat lama.
pause

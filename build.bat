@echo off
echo Building project...
cd C:\Users\DELL\OneDrive\Desktop\Backend\school_erp_backend
echo Clearing target directory...
powershell -Command "Remove-Item -Recurse -Force target -ErrorAction SilentlyContinue"
call mvnw.cmd clean package -DskipTests

echo Copying JAR to server...
scp target\school_erp_backend-0.0.1-SNAPSHOT.jar zohaib@72.61.229.22:/opt/app2/

echo Done! Now manually restart on server:
echo ssh zohaib@72.61.229.22
echo sudo systemctl restart schoolErp
pause
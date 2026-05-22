@echo off
echo Building project...
cd C:\Users\zohai\OneDrive\Desktop\school_erp_backend
call mvn clean package -DskipTests

echo Copying JAR to server...
scp target\school_erp_backend-0.0.1-SNAPSHOT.jar zohaib@72.61.229.22:/opt/app2/

echo Done! Now manually restart on server:
echo ssh zohaib@72.61.229.22
echo sudo systemctl restart schoolErp
pause
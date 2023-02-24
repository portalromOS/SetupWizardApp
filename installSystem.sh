#!/bin/bash

app_package="com.portalrom.setupwizard"
dir_app_name="SetupWizard"
MAIN_ACTIVITY="MainActivity"

ADB="adb"
ADB_SH="$ADB shell"

path_sysapp="/system/app"
apk_host="./app/build/outputs/apk/debug/app-debug.apk"
apk_name=$dir_app_name".apk"
apk_target_dir="$path_sysapp/$dir_app_name"
apk_target_sys="$apk_target_dir/$apk_name"
a=$($ADB shell "pm list packages | grep -i 'portalrom' | wc -l") #count packages with this name


##Delete previous APK
echo "DELETE PREVIOUS APK"
rm -f $apk_host

##Compile the APK: 
echo "COMPILE APK"
./gradlew assembleDebug  || exit -1 #exit on failure

#ADB ACTION – RUN BY HAND THE FIRST TIME
#$ADB root 2> /dev/null
#$ADB shell "avbctl disable-verification"
#$ADB reboot 2> /dev/null


##COPY APK
echo "COPY APK"
tmp_dir=/sdcard/tmp
$ADB root 2> /dev/null
$ADB remount  2> /dev/null
$ADB_SH "su 0 mount -o rw,remount /system"
$ADB_SH "chmod 777 /system"
$ADB_SH "mkdir -p $tmp_dir" 2> /dev/null
$ADB_SH "mkdir -p $apk_target_dir" 2> /dev/null
$ADB push $apk_host $tmp_dir/$apk_name 2> /dev/null
$ADB_SH "mv $tmp_dir/$apk_name $apk_target_sys"
$ADB_SH "rmdir $tmp_dir" 2> /dev/null

##Give Permissions
echo "PERMISSIONS"
$ADB_SH "chmod 755 $apk_target_dir"
$ADB_SH "chmod 644 $apk_target_sys"

##Unmount
echo "UNMOUNT"
$ADB_SH "mount -o remount,ro/"

##Stop the app
echo "STOP APP"
$ADB shell "am force-stop $app_package"

        

##NEED REBOOT TO INSTALL AS SYSTEM AFTER FIRST TIME
if [ "$a" -eq "0" ] 
then
	echo "REBOOT"
	$ADB reboot  2> /dev/null

	sleep 30

fi


##After reboot open app in emulator


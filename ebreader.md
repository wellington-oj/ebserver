# Ebserver requirements and setup

This document will present the steps necessary to setup Ebserver and run it on Individual Executions mode.
The information setup for all four examples will be presented below.

### requires: adb and node.js

## 1 Configuring Applications
    Find the development machine ip address.

### 1.1 Kotlin example
    on network/Repository.kt change the variable 'ip' to the ebserver ip
    on res/xml/network_security_config.xml include ebserver ip 

### 1.2 Flutter example
    on lib/main.dart change 'BASE_URL' to the ebserver ip

### 1.3 Ionic example
    on src/app/services/remote/http.service.ts change 'basePath' to the ebserver ip
    on android/app/src/main/res/xml/network_security_config.xml include ebserver ip 

### 1.4 React Native example
    on App.js change 'localhostip' to the ebserver ip
    android/app/src/main/res/xml/network_security_config.xml include ebserver ip 

## 3 Connecting Device
    Enable usb debugging on the device
    Connect the device to the development machine via usb cable
    The following commands should be done in the terminal:
    Type 'adb tcpip 5555'
    Type 'adb shell ip addr show wlan0' to find the ip address of the mobile device
    Type 'adb connect MOBILE_IP_ADDRESS'
    Unconnect the usb cable.

## 4 Starting Ebserver
    in the terminal, go to ebserver/src and type node index.js
    on the device, start the desired application

## 5 Advanced Configuring Ebserver
    go to ebserver/src/configurationsFolder and open defaultConfiguration.js
    edit exampleConfig so it better reflects the experiments that are running
    update module.exports experiments to reflect the previous changes
    if desired, change the number of experiments to be run on "start_execution" and "end_execution"
    go back to the application source code and update it




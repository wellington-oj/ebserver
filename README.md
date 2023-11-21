# Ebserver requirements and setup

This document will present the steps necessary to setup Ebserver and run it on Individual Executions mode and UI Executions mode.
The information setup for all four app examples will be presented below.

### requires: adb, node.js, and express

# Individual Executions mode

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
    enable usb debugging on the device
    connect the device to the development machine via usb cable
    the following commands should be done in the terminal:
      adb tcpip 5555
      adb shell ip addr show wlan0 -> to find the ip address of the mobile device
      adb connect MOBILE_IP_ADDRESS
    unconnect the usb cable.

## 4 Starting Ebserver
    in the terminal, go to ebserver/src and type 'node ebserver.js'
    on the device, start the application

## 5 Advanced Configuring Ebserver
    go to ebserver/src/configurationsFolder and open default.json
    change the default.json to better reflect the experiment being executed
    go back to the application source code and update it

# UI Executions mode

## 1 Configuring Applications
    find the development machine ip address.

## 2 Connecting Device
    enable usb debugging on the device
    connect the device to the development machine via usb cable
    the following commands should be done in the terminal:
      adb tcpip 5555
      adb shell ip addr show wlan0 -> to find the ip address of the mobile device
      adb connect MOBILE_IP_ADDRESS
    unconnect the usb cable.

## 3 Starting Ebserver
go to ebserver/src/testModule.js and change 'app_name' to the desired app
in the terminal, go to ebserver/src and type 'node ebserver.js METHOD TEST_CLASS PACKAGE SUFIX'
where TEST_CLASS is the class to be executed, PACKAGE the full package name of that class, METHOD the name of the method, and SUFIX is the suffix to be applied to the package name
to run the expresso example in this repository, run:

    node ebserver.js changeText_sameActivity ChangeTextBehaviorTest com.example.android.testing.espresso.BasicSample test

  

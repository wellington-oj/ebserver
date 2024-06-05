# Ebserver requirements and setup

This document will present the steps necessary to setup Ebserver and run it on Individual Executions mode and UI Executions mode.
The information setup for all four app examples will be presented below.

### requires: adb, node.js, and express

___
UI Executions mode
======
Uses test suites (such as Espresso and UI Automator) to execute the experiments

### 1 Connecting Device
    adb tcpip 5555
### 2 Run ebserver 
    node ebserver -ui
- It will automatically find the ip and connect the development machine to the mobile device


      Ebserver listening on port 3000!
      Device not connected via Wi-fi. Connecting to 192.168.1.240. Remove the USB cable and run ebserver again.
- Close ebserver
- Remove the usb cable

### 3 Starting Ebserver
- All configuration is done via json. You may find the json file at
    

      \ebserver\src\configUI\config.json
    
- Change the values on the json configuration file to the desired experiment
- Run ebserver again (make sure to use the ```-ui``` tag)


      node ebserver -ui

  

___
Individual Executions mode
======
Uses instrumentation to execute the tests


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


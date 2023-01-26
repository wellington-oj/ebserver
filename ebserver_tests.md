# Ebserver requirements and setup

This document will present the steps necessary to setup Ebserver and run it on Test Suite mode.
The information setup for all four examples will be presented below.

### requires: adb and node.js

## 1 Configuring Applications
    Find the development machine ip address.

## 2 Connecting Device
    Enable usb debugging on the device
    Connect the device to the development machine via usb cable
    The following commands should be done in the terminal:
    Type 'adb tcpip 5555'
    Type 'adb shell ip addr show wlan0' to find the ip address of the mobile device
    Type 'adb connect MOBILE_IP_ADDRESS'
    Unconnect the usb cable.

## 3 Starting Ebserver
    go to ebserver/src/testModule.js and change 'app_name' to the desired app
    in the terminal, go to ebserver/src and type 'node index.js TEST_CLASS PACKAGE METHOD'
    where TEST_CLASS it's the class to be executed, PACKAGE the full package name of that class, and METHOD the name of the method.

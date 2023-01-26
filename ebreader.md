# Ebreader setup

This document give some instructions on how to run the ebreader.

## requires: jdk 11+

# 1 Add Experiments and Apps
    To introduce new experiments or apps, use the template present in apps/experiments
    New experiments should implement 'ExpData' and new applications should implement 'AppName'
    The instanciation of AppName must use the package name of the application
    The instanciation of ExpData must defined the experiment, the input and a short version

# 2 Add Devices
    Add name of the device used as a new value to the enum DeviceNames

# 3 Run Ebreader
    On main/Main.java, change the device name and the number of files based on the experimental data
    Compile and execute main/Main.java
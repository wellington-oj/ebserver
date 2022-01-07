# Imports the monkeyrunner modules used by this program
import time
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
# device.installPackage('myproject/bin/MyApplication.apk')

# sets a variable with the package's internal name
package = 'com.android.chrome'

# sets a variable with the name of an Activity in the package
activity = 'com.google.android.apps.chrome.Main'

# sets the name of the component to start
#runComponent = package + '/' + activity

# Runs the component

#device.startActivity(component=runComponent)
#time.sleep(6)

#S6L-> #
#device.touch(100,100, 'DOWN')
#device.touch(100,400, 'UP')
device.drag((150, 1000), (150, 100), 0.6, 30)
#device.touch(1,1, 'DOWN_AND_UP')
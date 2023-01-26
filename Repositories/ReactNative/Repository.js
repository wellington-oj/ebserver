import type { Node } from 'react';
import DeviceInfo from 'react-native-device-info';

const APP_ID = "com.react.example.starter";
const FRAMEWORK = "REACT";
const TEST_TYPE = "example";
const ACTIVITY = '.MainActivity'
let hasCalled = false;

const localhostip = '192.168.1.68';

const App: () => Node = () => {
  useKeepAwake();
  let benchmark;
  const [isLoading, setLoading] = useState(true);

  const whatNow = async () => {
    if (hasCalled) { return; }
    console.log('http://' + localhostip + ':3000/' + 'what_now');
    try {
      let deviceModel = await DeviceInfo.getModel()
      const response = await fetch(
        'http://' + localhostip + ':3000/' + 'what_now',
        {
          method: 'GET',
          headers: {
            'device': deviceModel,
            'test_type': TEST_TYPE,
            'application_id': APP_ID,
            'framework': FRAMEWORK,
            'activity': ACTIVITY
          }
        }
      );
      benchmark = await response.text();
      hasCalled = true;
      console.log('executing: ' + benchmark);
      setLoading(false);


      //run program

      sendLogData();
    } catch (error) {
      console.error(error);
    }

  };

  whatNow();

  const sendLogData = async () => {
    try {
      console.log('http://' + localhostip + ':3000/' + 'logdata');
      let deviceModel = await DeviceInfo.getModel()
      const response = await fetch(
        'http://' + localhostip + ':3000/' + 'logdata',
        {
          method: 'GET',
          headers: {
            'device': deviceModel,
            'test_type': TEST_TYPE,
            'application_id': APP_ID,
            'framework': FRAMEWORK,
            'activity': ACTIVITY
          }
        }
      );
      const isDone = await response.text();
      done(isDone);
    } catch (error) {
      console.error(error);
    }
  };

  const done = async isDone => {
    if (isDone === 'done') {
      try {
        console.log('http://' + localhostip + ':3000/' + 'done');
        let deviceModel = await DeviceInfo.getModel()
        const response = await fetch(
          'http://' + localhostip + ':3000/' + 'done',
          {
            method: 'GET',
            headers: {
              'device': deviceModel,
              'test_type': TEST_TYPE,
              'application_id': APP_ID,
              'framework': FRAMEWORK,
              'activity': ACTIVITY
            }
          }
        );
      } catch (error) {
        console.error(error);
      }
    } else {
      console.log('not done?');
    }
  }
}
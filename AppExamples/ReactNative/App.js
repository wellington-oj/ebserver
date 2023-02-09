import React, {useState} from 'react';
import DeviceInfo from 'react-native-device-info';
import {useKeepAwake} from '@sayem314/react-native-keep-awake';
import type {Node} from 'react';

import {
  ActivityIndicator,
  FlatList,
  StyleSheet,
  View,
} from 'react-native';


let hasCalled = false;

const APP_ID = "com.react.example.starter";
const FRAMEWORK = "REACT";
const TEST_TYPE = "example";
const ACTIVITY = '.MainActivity';

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
            'application_id': APP_ID,
            'test_type': TEST_TYPE,
            'activity':  ACTIVITY,
            'framework': FRAMEWORK
          }
        }
      );
      benchmark = await response.text();
      hasCalled = true;
      console.log('executing: ' + benchmark);
      setLoading(false);
      await setupData(benchmark);
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
            'application_id': APP_ID,
            'test_type': TEST_TYPE,
            'activity':  ACTIVITY,
            'framework': FRAMEWORK
        }
      }
    );
    const isDone = await response.text();
    checkCorrectData(isDone);
  } catch (error) {
    console.error(error);
  }
};

const checkCorrectData = async isDone => {
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
            'application_id': APP_ID,
            'test_type': TEST_TYPE,
            'activity':  ACTIVITY,
            'framework': FRAMEWORK
          }
        }
      );
    } catch (error) {
      console.error(error);
    }
  } else {
    console.log('not done?');
  }
};


const setupData = async function (data) {
  const dataSplited = data.split('-');
  console.log(dataSplited);
  return new Promise(res => setTimeout(res, 5000));
}

return (
  <View style={styles.view}>
    {isLoading ? (
      <ActivityIndicator />
    ) : (
      <FlatList
        contentContainerStyle={styles.flatList}
        keyExtractor={item => item.id}
        renderItem={({item}) => (
          <View style={styles.imageView}>
            <p>{benchmark}</p>
          </View>
        )}
      />
    )}
  </View>
);

};

const styles = StyleSheet.create({
  view: {
    alignItems: 'center',
    justifyContent: 'center',
    textAlign: 'center',
    backgroundColor: 'white',
  },
  flatList: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingTop: '10%',
    flex: 1,
  },
  imageView: {
    padding: 5,
  },
});

const finish = function () {
  console.log('logdata');
  sendLogData();
}

export default App;

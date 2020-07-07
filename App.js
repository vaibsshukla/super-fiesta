/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { Component } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  NativeEventEmitter, NativeModules,
  DeviceEventEmitter,
} from 'react-native';

export default class App extends Component {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    this.eventListener = DeviceEventEmitter.addListener('JS-Event', function (e: Event) {
      console.log("Event" + JSON.stringify(e))
    });
  }

  componentWillUnmount() {
    this.eventListener.remove(); //Removes the listener
  }
  render() {
    return (
      <View>
        <Text>Please FirePush to Check Calling Functionality(Please Check Document For push Functionality)</Text>
      </View>
    )
  }

}


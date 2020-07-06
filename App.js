/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import { StyleSheet, NativeModules, View, TouchableOpacity, Text, Alert } from 'react-native';

var NativeMethodsCall = NativeModules.NativeMethods

const App = () => {
  return(
    <View style={styles.container}>
        <TouchableOpacity
          style={{ backgroundColor: 'green', paddingVertical: 10}}
          onPress = { () => NativeMethodsCall.addEvent('Hello This is First String', 'Hello This is Second Sttring', 6)}
        ><Text>Data Pass from Js To Native Ios</Text></TouchableOpacity>
        <TouchableOpacity
          style={{ backgroundColor: 'red',paddingVertical: 10}}
          onPress = {()=> NativeMethodsCall.findEvents((response)=> {alert(response)})}
        ><Text>Data Fetched from Native Ios to JavaScript Code</Text></TouchableOpacity>
    </View>
  )
}

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  }
})
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
  return (
    <View style={styles.container}>
      {/* <TouchableOpacity
        style={{ backgroundColor: 'grey', paddingVertical: 10, width: '60%', alignItems: 'center', borderRadius: 10 }}
        onPress={() => NativeMethodsCall.addEvent('Hello This is First String', 'Hello This is Second Sttring', 6)}
      ><Text style={{ fontSize: 16, color: 'white' }}>Data Pass from Js To Native Ios</Text></TouchableOpacity>
      <View style={{ width: '40%', height: 30 }}></View> */}

      <TouchableOpacity
        style={{ backgroundColor: 'grey', paddingVertical: 10, width: '60%', alignItems: 'center', borderRadius: 10 }}
        onPress={() => NativeMethodsCall.callMethod((response) => { alert(response) })}
      ><Text style={{ fontSize: 16, color: 'white' }}>Call Now</Text></TouchableOpacity>
      <View style={{ width: '40%', height: 30 }}></View>

      <TouchableOpacity
        style={{ backgroundColor: 'grey', paddingVertical: 10, width: '60%', alignItems: 'center', borderRadius: 10 }}
        onPress={() => NativeMethodsCall.callPickUp((response) => { alert(response) })}
      >
        <Text style={{ fontSize: 16, color: 'white' }}>Call Pick Up</Text>
      </TouchableOpacity>
      <View style={{ width: '40%', height: 30 }}></View>

      <TouchableOpacity
        style={{ backgroundColor: 'grey', paddingVertical: 10, width: '60%', alignItems: 'center', borderRadius: 10 }}
        onPress={() => NativeMethodsCall.callDecline((response) => { alert(response) })}
      >
        <Text style={{ fontSize: 16, color: 'white' }}>Call Decline</Text>
      </TouchableOpacity>
      <View style={{ width: '40%', height: 30 }}></View>
    </View>
  )
}

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: 10
  }
})
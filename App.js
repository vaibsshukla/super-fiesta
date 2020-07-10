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
       <View style={{ flex: .5, backgroundColor: 'green', flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>
        <TouchableOpacity
          style={{ backgroundColor: 'grey', paddingVertical: 10, width: '20%', alignItems: 'center', borderRadius: 10 }}
          onPress={() => NativeMethodsCall.accept('Accept Pass')}
        ><Text style={{ fontSize: 16, color: 'white' }}>Accept</Text></TouchableOpacity>
      <View style={{ width: '40%', height: 30 }}></View>
        <TouchableOpacity
          style={{ backgroundColor: 'grey', paddingVertical: 10, width: '20%', alignItems: 'center', borderRadius: 10 }}
          onPress={() => NativeMethodsCall.decline('Decline Pass')}
        ><Text style={{ fontSize: 16, color: 'white' }}>Decline</Text></TouchableOpacity>
      </View>


      <View style={{ flex: .5, backgroundColor: 'blue', flexDirection: 'row',justifyContent: 'center', alignItems: 'center' }}>
        <TouchableOpacity
          style={{ backgroundColor: 'grey', width: '20%', alignItems: 'center', borderRadius: 10, paddingVertical: 10 }}
          onPress={() => NativeMethodsCall.acceptMethod((response) => { alert(response) })}
        >
          <Text style={{ fontSize: 16, color: 'white' }}>Accept</Text>
        </TouchableOpacity>
        <View style={{ width: '40%', height: 30 }}></View>

        <TouchableOpacity
          style={{ backgroundColor: 'grey', width: '20%', alignItems: 'center', borderRadius: 10 ,paddingVertical: 10 }}
          onPress={() => NativeMethodsCall.declineMethod((response) => { alert(response) })}
        >
          <Text style={{ fontSize: 16, color: 'white' }}>Decline</Text>
        </TouchableOpacity>

      </View>
    </View>
  )
}

export default App;
const styles = StyleSheet.create({
  container: {
    flex: 1,
  }
})



// import React, { Component } from 'react';
// import { StyleSheet, Text, View, requireNativeComponent, Alert } from 'react-native';

// const Bulb = requireNativeComponent("Bulb")

// export default class App extends Component {
//   constructor(props) {
//     super(props);
//     this.state = { acceptCall: 'hii' };
//   }
//   onDataSetInState = getData => {
//     alert(getData.nativeEvent.acceptCall)
//     this.setState({ isOn: getData.nativeEvent.acceptCall });
//   }
//   render() {
//     return (
//       <View style={styles.container}>
//         <View style={styles.top}></View>
//         <Bulb style={styles.middle}  onStatusChange={this.onDataSetInState} />
//         <View style={{ width: '100%', height: 10, backgroundColor: 'blue'}}></View>
//         {/* <Bulb style={styles.bottom}  onStatusChange={this.onDataSetInState} /> */}
//       </View>
//     );
//   }
// }
// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#F5FCFF',
//   },
//   top: {
//     flex: .2,
//     alignItems: "center",
//     justifyContent: "center",
//     backgroundColor: 'green'
//   },
//   middle: {
//     flex: .4,
//     alignItems: "center",
//     justifyContent: "center",
//   },
//   bottom: {
//     flex: .4,
//     alignItems: "center",
//     justifyContent: "center",
//   },
// });

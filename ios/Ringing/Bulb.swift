//
//  Bulb.swift
//  Call_Bridging
//
//  Created by Vibhishan on 7/7/20.
//

import Foundation
@objc(Bulb)
class Bulb: RCTViewManager {
  override func view() -> UIView! {
    return BulbView()
  }
  
  @objc
  override static func requiresMainQueueSetup() -> Bool {
  return true
  }
}

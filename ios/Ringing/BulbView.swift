//
//  BulbView.swift
//  Call_Bridging
//
//  Created by Vibhishan on 7/7/20.
//

import UIKit
class BulbView: UIView {
  @objc var onStatusChange: RCTDirectEventBlock?
  @objc var acceptCall: String = "acceptCall"  {
    didSet {
      button.setTitle(String(describing: "Accept"), for: .normal)
      button.backgroundColor = .yellow
//      accept.setTitle(String(describing: "Accept"), for: .normal)
//      accept.backgroundColor = .yellow
    }
  }
  override init(frame: CGRect) {
    super.init(frame: frame)
    self.addSubview(button)
//    self.addSubview(accept)
  }
  required init?(coder aDecoder: NSCoder) {
    fatalError("init has not been implemented")
  }
  lazy var button: UIButton = {
    let button = UIButton.init(type: UIButton.ButtonType.system)
    button.titleLabel?.font = UIFont.systemFont(ofSize: 20)
    button.autoresizingMask = [.flexibleWidth, .flexibleHeight]
    button.addTarget(
      self,
      action: #selector(changeBulbStatus),
      for: .touchUpInside
    )
    return button
  }()
  
//  lazy var accept: UIButton = {
//     let accept = UIButton.init(type: UIButton.ButtonType.system)
//     accept.titleLabel?.font = UIFont.systemFont(ofSize: 20)
//     accept.autoresizingMask = [.flexibleWidth, .flexibleHeight]
//     accept.addTarget(
//       self,
//       action: #selector(changeBulbStatus),
//       for: .touchUpInside
//     )
//     return accept
//   }()
  
  @objc func changeBulbStatus() {
    onStatusChange!(["acceptCall": acceptCall])
  }
  
//  @objc func changeBulbStatus() {
//    isOn = !isOn as Bool
//    onStatusChange!(["isOn": isOn])
//  }
}

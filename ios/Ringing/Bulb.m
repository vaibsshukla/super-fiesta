//
//  Bulb.m
//  Call_Bridging
//
//  Created by Vibhishan on 7/7/20.
//

#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(Bulb, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(isOn, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onStatusChange, RCTDirectEventBlock)

@end

//
//  NativeMethods.m
//  Call_Bridging
//
//  Created by Vibhishan on 7/6/20.
//

#import "NativeMethods.h"
#import <React/RCTLog.h>


@implementation NativeMethods

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(addEvent:(NSString *)name location:(NSString *)location address:(int)address)
{
  RCTLogInfo(@"Pretending to create an event %@ at %@ int %d", name, location, address);
  myVar = name;
}

RCT_EXPORT_METHOD(callMethod:(RCTResponseSenderBlock)callback)
{
//  callback(@[@"Native Ios"] intArgumentDemo: (int) arg);
//  callback(@[@"Native Ios", @5]);
  callback(@[@"Calling"]);
}

RCT_EXPORT_METHOD(callPickUp:(RCTResponseSenderBlock)callback)
{
//  callback(@[@"Native Ios"] intArgumentDemo: (int) arg);
//  callback(@[@"Native Ios", @5]);
  callback(@[@"Call Pick Up"]);
}

RCT_EXPORT_METHOD(callDecline:(RCTResponseSenderBlock)callback)
{
//  callback(@[@"Native Ios"] intArgumentDemo: (int) arg);
//  callback(@[@"Native Ios", @5]);
  callback(@[@"Call Declined"]);
}
@end

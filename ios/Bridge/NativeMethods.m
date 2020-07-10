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

RCT_EXPORT_METHOD(accept:(NSString *)acceptData)
{
  RCTLogInfo(@"Pretending to create an event %@",acceptData);
  myVar = acceptData;
}

RCT_EXPORT_METHOD(decline:(NSString *)declineData)
{
  RCTLogInfo(@"Pretending to create an event %@",declineData);
  myVar = declineData;
}



//MARK:-this is for call back response
RCT_EXPORT_METHOD(acceptMethod:(RCTResponseSenderBlock)callback)
{
//  callback(@[@"Native Ios"] intArgumentDemo: (int) arg);
//  callback(@[@"Native Ios", @5]);
  callback(@[@"Call Pick Up"]);
}

RCT_EXPORT_METHOD(declineMethod:(RCTResponseSenderBlock)callback)
{
//  callback(@[@"Native Ios"] intArgumentDemo: (int) arg);
//  callback(@[@"Native Ios", @5]);
  callback(@[@"Call Declined"]);
}
@end

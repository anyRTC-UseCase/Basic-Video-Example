//
//  ItemModel.h
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ItemModel : NSObject

@property (nonatomic ,copy) NSString *uid;

@property (nonatomic ,strong) ARtcEngineKit *engineKit;

@property (nonatomic ,assign) BOOL isSelf;

@end

NS_ASSUME_NONNULL_END

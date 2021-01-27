//
//  VideoShowCell.h
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import <UIKit/UIKit.h>
#import "ItemModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface VideoShowCell : UICollectionViewCell

+ (instancetype)createVideoShowCellWithCollectionView:(UICollectionView *)collection IndexPath:(NSIndexPath *)indexPath;

@property (nonatomic ,strong) ItemModel *model;

@end

NS_ASSUME_NONNULL_END

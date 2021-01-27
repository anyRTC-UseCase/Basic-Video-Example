//
//  VideoShowCell.m
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "VideoShowCell.h"

@interface VideoShowCell ()

@property (weak, nonatomic) IBOutlet UIView *videoView;

@end

@implementation VideoShowCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

+ (instancetype)createVideoShowCellWithCollectionView:(UICollectionView *)collection IndexPath:(NSIndexPath *)indexPath {
    VideoShowCell *cell = [collection dequeueReusableCellWithReuseIdentifier:NSStringFromClass([self class]) forIndexPath:indexPath];
    return cell;
}


- (void)setModel:(ItemModel *)model {
    ARtcVideoCanvas *canvas = [[ARtcVideoCanvas alloc] init];
    canvas.view = self.videoView;
    canvas.renderMode = ARVideoRenderModeFit;
    canvas.uid = model.uid;
    model.isSelf ? [model.engineKit setupLocalVideo:canvas] : [model.engineKit setupRemoteVideo:canvas];
}


@end

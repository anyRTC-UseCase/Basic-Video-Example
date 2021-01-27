//
//  RoomViewController.m
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#define APPID               @"177e21c0d1641291c34e46e1198bd49a"

#import "RoomViewController.h"
#import "VideoShowCell.h"
#import "ItemModel.h"
@interface RoomViewController ()<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,ARtcEngineDelegate>

@property (weak, nonatomic) IBOutlet UICollectionView *collection;

@property (weak, nonatomic) IBOutlet UIButton *cameraBtn;
@property (weak, nonatomic) IBOutlet UIButton *closeBtn;
@property (weak, nonatomic) IBOutlet UIButton *micBtn;

@property (nonatomic ,strong) NSMutableArray *dataArray;

@property (nonatomic ,strong) ARtcEngineKit *engineKit;
@end

@implementation RoomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.channelId;
    [self initUI];
    [self initARtcKit];
    [self.engineKit enableVideo];
    [self.engineKit joinChannelByToken:@"" channelId:self.channelId uid:self.userId joinSuccess:nil];
//    [self.engineKit setEnableSpeakerphone:YES];
}


- (void)initUI {

    [self.collection registerNib:[UINib nibWithNibName:NSStringFromClass([VideoShowCell class]) bundle:[NSBundle mainBundle]] forCellWithReuseIdentifier:NSStringFromClass([VideoShowCell class])];
    
    [self.micBtn setImage:IMG(@"img_audio_open") forState:UIControlStateNormal];
    [self.micBtn setImage:IMG(@"img_audio_close") forState:UIControlStateSelected];
    
    [self.cameraBtn setImage:IMG(@"img_switch_click") forState:UIControlStateNormal];
    [self.cameraBtn setImage:IMG(@"img_switch") forState:UIControlStateSelected];
}

- (void)initARtcKit {
    self.engineKit = [ARtcEngineKit sharedEngineWithAppId:APPID delegate:self];
    [self.engineKit setEnableSpeakerphone:YES];
    [self.engineKit setChannelProfile:ARChannelProfileCommunication];
}

- (void)setLocalVideo {
    ItemModel *model = [[ItemModel alloc] init];
    model.engineKit = self.engineKit;
    model.uid = self.userId;
    model.isSelf = YES;
    [self.dataArray addObject:model];
    [self.collection reloadData];
}



- (NSMutableArray *)dataArray {
    if (!_dataArray) {
        _dataArray = [[NSMutableArray alloc] init];
    }
    return _dataArray;
}


//MARK: ---UICollectionViewDelegate/UICollectionViewDataSource/UICollectionViewDelegateFlowLayout

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataArray.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    VideoShowCell *cell = [VideoShowCell createVideoShowCellWithCollectionView:collectionView IndexPath:indexPath];
    cell.model = self.dataArray[indexPath.row];
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (self.dataArray.count <= 4) {
        return CGSizeMake((APP_WIDTH - 30)/2, (APP_WIDTH - 30)/2 * 640 / 480);
    } else if (self.dataArray.count > 4 && self.dataArray.count <= 9) {
        return CGSizeMake((APP_WIDTH - 40)/2, (APP_WIDTH - 40)/2 * 640 / 480);
    } else if (self.dataArray.count > 9 && self.dataArray.count <= 16) {
        return CGSizeMake((APP_WIDTH - 50)/2, (APP_WIDTH - 50)/2 * 640 / 480);
    } else if (self.dataArray.count > 16 && self.dataArray.count <= 25) {
        return CGSizeMake((APP_WIDTH - 60)/2, (APP_WIDTH - 60)/2 * 640 / 480);
    } else if (self.dataArray.count > 25 && self.dataArray.count <= 36) {
        return CGSizeMake((APP_WIDTH - 70)/2, (APP_WIDTH - 70)/2 * 640 / 480);
    } else {
        return CGSizeMake((APP_WIDTH - 110)/2, (APP_WIDTH - 110)/2 * 640 / 480);
    }
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(0, 10, 10, 10);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return 0;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return 0;
}

//MARK: - ARtcEngineDelegate

///用户加入RTC频道回调
- (void)rtcEngine:(ARtcEngineKit *)engine didJoinChannel:(NSString *)channel withUid:(NSString *)uid elapsed:(NSInteger)elapsed {
//    ItemModel *model = [[ItemModel alloc] init];
//    model.uid = uid;
//    [self.dataArray addObject:model];
//    [self.collection reloadData];
}

- (void)rtcEngine:(ARtcEngineKit *_Nonnull)engine didOfflineOfUid:(NSString *_Nonnull)uid reason:(ARUserOfflineReason)reason {
    for (ItemModel *model in self.dataArray) {
        if ([model.uid isEqualToString:uid]) {
            [self.dataArray removeObject:model];
            break;;
        }
    }
    [self.collection reloadData];
}

- (void)rtcEngine:(ARtcEngineKit * _Nonnull)engine firstLocalVideoFrameWithSize:(CGSize)size elapsed:(NSInteger)elapsed {
    [self setLocalVideo];
}

- (void)rtcEngine:(ARtcEngineKit *_Nonnull)engine firstRemoteVideoFrameOfUid:(NSString *_Nonnull)uid size:(CGSize)size elapsed:(NSInteger)elapsed {
    
}

- (void)rtcEngine:(ARtcEngineKit *_Nonnull)engine firstRemoteVideoDecodedOfUid:(NSString *_Nonnull)uid size:(CGSize)size elapsed:(NSInteger)elapsed {
    ItemModel *model = [[ItemModel alloc] init];
    model.engineKit = engine;
    model.uid = uid;
    model.isSelf = NO;
    [self.dataArray addObject:model];
    [self.collection reloadData];
}

- (IBAction)MicAction:(id)sender {
    self.micBtn.selected = !self.micBtn.selected;
    [self.engineKit enableLocalAudio:self.micBtn.selected];
}

- (IBAction)CloseAction:(id)sender {
    [self.engineKit leaveChannel:nil];
    self.engineKit.delegate = nil;
    self.engineKit = nil;
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)CameraAction:(id)sender {
//    UIButton *currentBtn = (UIButton *)sender;
//    currentBtn.selected = !currentBtn.selected;
    self.cameraBtn.selected = !self.cameraBtn.selected;
    [self.engineKit switchCamera];
}

@end

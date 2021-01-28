//
//  HomeViewController.m
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "HomeViewController.h"
#import "RoomViewController.h"

@interface HomeViewController ()
{
    NSString            *_uid;
}
@property (weak, nonatomic) IBOutlet UITextField *channelTF;
@property (weak, nonatomic) IBOutlet UIButton *joinBtn;

@end

@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _uid = [self getRandomUid];
    self.view.backgroundColor = [UIColor whiteColor];
}


- (IBAction)JoinChannelAction:(id)sender {
    [self.view endEditing:YES];
    
    if ([self.channelTF.text isEqualToString:@""]) {
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"提示" message:@"频道id不能为空" preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *sureAction = [UIAlertAction actionWithTitle:@"确认" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:sureAction];
        [self presentViewController:alert animated:YES completion:nil];
    } else {
        UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
        RoomViewController *vc = [sb instantiateViewControllerWithIdentifier:@"RoomViewController"];
        vc.userId = _uid;
        vc.channelId = self.channelTF.text;
        [self.navigationController pushViewController:vc animated:YES];
    }
}



- (NSString *)getRandomUid {
    NSArray *randoms = [[NSArray alloc] initWithObjects:@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9", nil];
    NSString *resultStr = @"";
    for (int i = 0; i < 6; i ++) {
        NSInteger index = arc4random()%(randoms.count - 1);
        NSString *randomStr = randoms[index];
        resultStr = [resultStr stringByAppendingString:randomStr];
    }
    return resultStr;
}


- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

@end

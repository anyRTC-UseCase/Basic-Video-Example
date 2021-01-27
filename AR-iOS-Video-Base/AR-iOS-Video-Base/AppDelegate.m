//
//  AppDelegate.m
//  AR-iOS-Video-Base
//
//  Created by anyRTC on 2021/1/26.
//

#import "AppDelegate.h"
#import "HomeViewController.h"
@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    
    [[UINavigationBar appearance] setBarTintColor:kUIColorFromRGB(0x121F2B)];
    [[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
    [[UINavigationBar appearance] setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor whiteColor],NSFontAttributeName:[UIFont systemFontOfSize:18]}];
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    HomeViewController *vc = [storyBoard instantiateViewControllerWithIdentifier:@"HomeViewController"];
    
    self.window.rootViewController = [[UINavigationController alloc] initWithRootViewController:vc];
    
    NSError *error;
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayback error:&error];
    [session setActive:YES error:&error];
    
    return YES;
}



@end

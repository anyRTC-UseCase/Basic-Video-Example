//
//  ARConstanst.h
//  AR-iOS-Voice-Base
//
//  Created by anyRTC on 2021/1/25.
//

// iPhone X
#define  iOS11 (APP_WIDTH >= 375.f && APP_HEIGHT >= 812.f ? YES : NO)
#define  TABBARSAFE         (iOS11 ? 34.f : 0.f)
// Status bar & navigation bar height.
#define  NAVIGHTIONHEIGHT  (iOS11 ? 88.f : 64.f)
#define STATEHEIGHT        (iOS11 ? 34.f : 20.f)
#define NEWTABBARHEIGHT     (IS_IPhoneX ? 83 : 48)




//MARK: - 视图宽高宏定义
//屏幕宽高
#define APP_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define APP_HEIGHT ([[UIScreen mainScreen] bounds].size.height)
//获取左上角原点的位置
#define VIEW_TX(view) (view.frame.origin.x)
#define VIEW_TY(view) (view.frame.origin.y)

#pragma mark ----Size ,X,Y, View ,Frame
//获取View 的大小
#define VIEW_W(view)  (view.frame.size.width)
#define VIEW_H(view)  (view.frame.size.height)

#define VIEW_BX(view) (view.frame.origin.x + view.frame.size.width)
#define VIEW_BY(view) (view.frame.origin.y + view.frame.size.height)

#define FRAME_TX(frame)  (frame.origin.x)
#define FRAME_TY(frame)  (frame.origin.y)

#define FRAME_W(frame)  (frame.size.width)
#define FRAME_H(frame)  (frame.size.height)
        

#define FONT(s)       [UIFont systemFontOfSize:s]



//MARK: - 颜色相关

#define RGB(r, g, b) \
[UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1]

#define RGBA(r, g, b, a) \
[UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:(a)]

//颜色简写
#define kUIColorFromRGB(rgbValue) [UIColor \
colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 \
green:((float)((rgbValue & 0xFF00) >> 8))/255.0 \
blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]


//MARK: ---- UIImage  UIImageView  functions
#define IMG(name) [UIImage imageNamed:name]

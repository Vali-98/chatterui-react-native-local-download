#import "ReactNativeLocalDownload.h"
#import <UIKit/UIKit.h>

@implementation ReactNativeLocalDownload
RCT_EXPORT_MODULE()

- (void)localDownload:(NSString *)uri
             resolve:(RCTPromiseResolveBlock)resolve
              reject:(RCTPromiseRejectBlock)reject
{
  dispatch_async(dispatch_get_main_queue(), ^{
    NSURL *fileURL = [NSURL fileURLWithPath:uri];
    if (![fileURL isFileURL] || ![[NSFileManager defaultManager] fileExistsAtPath:uri]) {
      reject(@"invalid_file", @"The file URI is not valid or the file does not exist", nil);
      return;
    }

    UIActivityViewController *controller = [[UIActivityViewController alloc]
        initWithActivityItems:@[fileURL]
        applicationActivities:nil];

    UIViewController *rootVC = UIApplication.sharedApplication.delegate.window.rootViewController;
    [rootVC presentViewController:controller animated:YES completion:nil];

    resolve(nil);
  });
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeReactNativeLocalDownloadSpecJSI>(params);
}

@end

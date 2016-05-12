/*
* Copyright 2016 Gowtham Ganesh Nayak,
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Purpose: Assignment 2 Submission
* This example demonstrates different states of an iOS application. Additonally
* it prints out a log statement on the console when the application enters
* each of the state.
*
* SER 598 Mobile Applications
* @author gnayak2@asu.edu
* @version January 2016
*/

import UIKit


// This will have all the methods of iOS Application LifeCycle. Respective methods have been implemented to demonstrate each of the state. Logs will be printed on the console.

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        // First method launched after the application is launched.
        // TODO: Lauch the app for the first time.
        NSLog("AppLifeCycleDemo executed application() method ")
        return true
    }

    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it 
        // begins the transition to the background state.Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
        // This method is called when applicaiton is moved to the background. This method is executed just before it moves to the background.
        // TODO: Press the home button.
        NSLog("AppLifeCycleDemo executed applicationWillResignActive() method ")
    }

    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
        // This method is called after the application enters background. It also means that the app can be suspended.
        // TODO: Press the home button.
        
        NSLog("AppLifeCycleDemo executed applicationDidEnterBackground() method ")
    }

    func applicationWillEnterForeground(application: UIApplication) {
        // Lets you know that your app is moving out of the background and back into the foreground, but that it is not yet active.
        // This method is called when app is moved from background to foreground. But the app is not active yet.
        // TODO: Relaunh the app.
        NSLog("AppLifeCycleDemo executed applicationWillEnterForeground() method ")
    }

    func applicationDidBecomeActive(application: UIApplication) {
        // This method is called when the app is going to come to foreground.
        // TODO: ReLauch the app.
        NSLog("AppLifeCycleDemo executed applicationDidBecomeActive() method ")
    }

    func applicationWillTerminate(application: UIApplication) {
        // This method when the app is terminated. This method is executed just before termination.
        // TODO: Press the home button twice and terminate the app.
        NSLog("AppLifeCycleDemo executed applicationWillTerminate() method ")
    }


}


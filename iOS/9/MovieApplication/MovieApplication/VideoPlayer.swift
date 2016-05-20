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
 * Purpose: Assignment 9 Submission
 * This example fetches movie descriptions from the json rpc server to initialize
 * the core data and also streams the media from the media server.
 * SER 598 Mobile Applications
 * @author gnayak2@asu.edu
 * @version April 2016
 */

import UIKit
import AVKit
import AVFoundation

class VideoPlayer: UIViewController, NSURLSessionDelegate {
    
    var streamer_host = "localhost"
    var streamer_port = "8888"
    var media:String?
    
    
    
    //var downloadBG:NSURLSessionTask!
    //var backSess:NSURLSession!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        // get the host and port from Info.plist
        
        if let infoPlist = NSBundle.mainBundle().infoDictionary {
            print("here")
            let ip = ((infoPlist["MEDIAServerURL"]) as?  String!)!
            let port = ((infoPlist["MEDIAServePort"]) as?  String!)!
            
            self.streamer_host = ip
            self.streamer_port = port
            print(self.streamer_host)
            print(self.streamer_port)
        }else{
            NSLog("error getting urlString from info.plist")
        }
        
        let urlString:String = "http://\(self.streamer_host):\(self.streamer_port)/" + media!
        NSLog("viewDidLoad using url: \(urlString)")
        // download the video to a file (completely) before playing
        downloadVideo(urlString)

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // download the video in the background using NSURLSession.
    func downloadVideo(urlString: String){
        let bgConf = NSURLSessionConfiguration.backgroundSessionConfigurationWithIdentifier(media!)
        let backSess = NSURLSession(configuration: bgConf, delegate: self, delegateQueue:NSOperationQueue.mainQueue())
        let aUrl = NSURL(string: urlString)!
        let downloadBG = backSess.downloadTaskWithURL(aUrl)
        downloadBG.resume()
        
    }
    
    // play the movie from a file url
    func playMovieAtURL(fileURL: NSURL){
        let player = AVPlayer(URL: fileURL)
        let playCont = AVPlayerViewController()
        playCont.player = player
        fetchPresentableViewController().presentViewController(playCont, animated: true) {
            playCont.player!.play()
        }
    }
    
    func fetchPresentableViewController() -> UIViewController {
        var topCont = UIApplication.sharedApplication().keyWindow?.rootViewController
        while((topCont!.presentedViewController) != nil) {
            topCont = topCont?.presentedViewController
        }
        return topCont!
    }
    
    // functions for NSURLSessionDelegate
    func URLSession(session: NSURLSession,
        downloadTask: NSURLSessionDownloadTask,
        didFinishDownloadingToURL location: NSURL){
            
            let path = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
            let documentDirectoryPath:String = path[0]
            let fileMgr = NSFileManager()
            let destinationURLForFile = NSURL(fileURLWithPath: documentDirectoryPath.stringByAppendingString("/" + media!))
            
            if fileMgr.fileExistsAtPath(destinationURLForFile.path!) {
                NSLog("destination file url: \(destinationURLForFile.path!) exists. Deleting")
                do {
                    try fileMgr.removeItemAtURL(destinationURLForFile)
                }catch{
                    NSLog("error removing file at: \(destinationURLForFile)")
                }
            }
            do {
                try fileMgr.moveItemAtURL(location, toURL: destinationURLForFile)
                // show file
                NSLog("download and save completed: \(destinationURLForFile.path!)")
                playMovieAtURL(destinationURLForFile)
            }catch{
                NSLog("An error occurred while moving file to destination url")
            }
    }
    
    func URLSession(session: NSURLSession,
        downloadTask: NSURLSessionDownloadTask,
        didWriteData bytesWritten: Int64,
        totalBytesWritten: Int64,
        totalBytesExpectedToWrite: Int64){
            NSLog("did write portion of file: \(Float(totalBytesWritten)/Float(totalBytesExpectedToWrite))")
    }
    
}

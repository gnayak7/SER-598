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
* Purpose: Assignment 4 Submission
* This example demonstrates Table View and Picker
* SER 598 Mobile Applications
* @author gnayak2@asu.edu
* @version March 2016
*/

import Foundation

// This class is wrapper to make  json rpc request

class RPCRequestAndResponse {
    var SEVER_URL:String?
    
    init(methodInformation:String, params:[String], id:String, doSomething: (someVar:[String : AnyObject]) -> Void) {
        
        if let infoPlist = NSBundle.mainBundle().infoDictionary {
            self.SEVER_URL = ((infoPlist["ServerURLString"]) as?  String!)!
            NSLog("The default urlString from info.plist is \(self.SEVER_URL)")
        }else{
            NSLog("error getting urlString from info.plist")
        }
        
        let dict:[String: AnyObject] = ["jsonrpc":"2.0", "method":methodInformation,"params":params,"id":id]
        do {
            let reqData:NSData = try NSJSONSerialization.dataWithJSONObject(dict, options: NSJSONWritingOptions(rawValue: 0))
            self.asyncHttpPostJSON(self.SEVER_URL!, data: reqData, callback: doSomething)
        } catch let error as NSError {
            print(error)
        }
    }
    
    init(methodInformation:String, params:String, id:String, doSomething: (someVar:[String : AnyObject]) -> Void) {
        
        if let infoPlist = NSBundle.mainBundle().infoDictionary {
            self.SEVER_URL = ((infoPlist["ServerURLString"]) as?  String!)!
            NSLog("The default urlString from info.plist is \(self.SEVER_URL)")
        }else{
            NSLog("error getting urlString from info.plist")
        }
        
        var ary = [String]()
        ary.append(params)
        let dict:[String: AnyObject] = ["jsonrpc":"2.0", "method":methodInformation,"params":ary,"id":id]
        
        do {
            let reqData:NSData = try NSJSONSerialization.dataWithJSONObject(dict, options: NSJSONWritingOptions(rawValue: 0))
            self.asyncHttpPostJSON(self.SEVER_URL!, data: reqData, callback: doSomething)
        } catch let error as NSError {
            print(error)
        }
    }
    
    func asyncHttpPostJSON(url: String,  data: NSData,
        callback: (someVar:[String : AnyObject]) -> Void) {
            
            let request = NSMutableURLRequest(URL: NSURL(string: url)!)
            request.HTTPMethod = "POST"
            request.addValue("application/json",forHTTPHeaderField: "Content-Type")
            request.addValue("application/json",forHTTPHeaderField: "Accept")
            request.HTTPBody = data
            sendHttpRequest(request, callback: callback)
    }
    
    func sendHttpRequest(request: NSMutableURLRequest,
        callback: (someVar:[String : AnyObject]) -> Void) {
            let task = NSURLSession.sharedSession().dataTaskWithRequest(request) {
                (data, response, error) -> Void in
                if (error != nil) {
                    NSLog("Network Error: ", "Error in reading")
                } else {
                    dispatch_async(dispatch_get_main_queue(),
                        {
                            let str = NSString(data: data!, encoding: NSUTF8StringEncoding)! as String
                            if let data: NSData = str.dataUsingEncoding(NSUTF8StringEncoding){
                                do{
                                    let movieGenreAndNames = try NSJSONSerialization.JSONObjectWithData(data,options:.MutableContainers) as! [String:AnyObject]
                                    callback(someVar: movieGenreAndNames)
                                } catch {
                                    print("unable to convert to dictionary")
                                }
                            }
                    })
                }
            }
            task.resume()
    }
}
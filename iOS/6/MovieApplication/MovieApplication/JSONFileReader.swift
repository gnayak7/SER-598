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

// This class reads json file and return an array of movie dictionary objects
class ReadJSON {
    
    // some data structer maintian an array of json objects
    //    var jsonArray: Array<Dictionary<String,AnyObject>>?
    var jsonArray:Array<Dictionary<String,AnyObject>>
    
    init(fileName: String) {
        let path = NSBundle.mainBundle().pathForResource(fileName, ofType:"json")!
        let jsonData = NSData(contentsOfFile: path)!
        
        do {
            let jsonResult = try NSJSONSerialization.JSONObjectWithData(jsonData, options: NSJSONReadingOptions.MutableContainers)
            self.jsonArray = (jsonResult as? [[String:AnyObject]])!
        } catch {
            self.jsonArray = [["dummy":"dummy"]]
        }
    }
    
    func getJSONArray() -> Array<Dictionary<String,AnyObject>> {
        return self.jsonArray
    }
}
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
* Purpose: Assignment 1 Submission
* This example shows how to make a HTTP Request to get a JSON Response and parse to fields and
* extract information and display it on a screen.
*
* SER 598 Mobile Applications
* @author gnayak2@asu.edu
* @version January 2016
*/

import Foundation
/*
This is the container class to hold all the Movie Information. It extracts all the fields from the JSON String and hold it in its varialbes.
*/
class Movie {
    var title: String
    var year: String
    var rated: String
    var released: String
    var runtime: String
    var actors: String
    var genre: String
    var plot: String
    
    init (jsonStr: String){
        self.title = ""
        self.year = ""
        self.actors = ""
        self.genre = ""
        self.plot = ""
        self.rated = ""
        self.released = ""
        self.runtime = ""
        
        if let data: NSData = jsonStr.dataUsingEncoding(NSUTF8StringEncoding) {
            do{
                let dict = try NSJSONSerialization.JSONObjectWithData(data,options:.MutableContainers) as?[String:AnyObject]
                self.title = (dict!["Title"] as? String)!
                self.year = (dict!["Year"] as? String)!
                self.rated = (dict!["Rated"] as? String)!
                self.released = (dict!["Released"] as? String)!
                self.actors = (dict!["Actors"] as? String)!
                self.genre = (dict!["Genre"] as? String)!
                self.plot = (dict!["Plot"] as? String)!
                self.runtime = (dict!["Runtime"] as? String)!
            } catch {
                print("unable to convert to dictionary")
                
            }
        }
    }
    
    func getTitle() -> String {
        return self.title
    }
    
    func getYear() -> String {
        return self.year
    }
    func getRated() -> String {
        return self.rated
    }

    func getReleased() -> String {
        return self.released
    }

    func getActors() -> String {
        return self.actors
    }

    func getGenre() -> String {
        return self.genre
    }

    func getRuntime() -> String {
        return self.runtime
    }
    func getPlot() -> String {
        return self.plot
    }

    func toJsonString() -> String {
        var jsonStr = "";
        let dict = ["Title": self.title, "Year": year, "Rated": self.rated, "Runtime": self.runtime, "Released": self.released, "Genre": self.genre, "Actors": self.actors,"Plot": self.plot]
        do {
            let jsonData = try NSJSONSerialization.dataWithJSONObject(dict, options: NSJSONWritingOptions.PrettyPrinted)
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: NSUTF8StringEncoding)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }

}
    

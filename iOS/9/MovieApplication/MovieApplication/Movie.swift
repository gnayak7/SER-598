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
import Foundation

// This class is Movie Class which hold movie descriptions
class Movie {
    var id:String
    var title: String
    var year: String
    var rated: String
    var released: String
    var actors: String
    var genre: String
    var plot: String
    var runtime:String
    var media: String
    
    
    // this is from OMDB so set media to some default value
    init (jsonString: String) {
        self.title = ""
        self.rated = ""
        self.released = ""
        self.actors = "-"
        self.genre = ""
        self.plot = "-"
        self.year = ""
        self.runtime = ""
        self.media = ""
        self.id = NSUUID().UUIDString
        
        if let data: NSData = jsonString.dataUsingEncoding(NSUTF8StringEncoding) {
            do{
                let dict = try NSJSONSerialization.JSONObjectWithData(data, options:.MutableContainers) as! [String:AnyObject]
                
                let movieFound = dict["Response"] as! String
                
                if movieFound == "True" {
                    self.title = dict["Title"] as! String
                    self.year = dict["Year"] as! String
                    self.rated = dict["Rated"] as! String
                    self.released = dict["Released"] as! String
                    self.genre = dict["Genre"] as! String
                    self.actors = dict["Actors"] as! String
                    self.plot = dict["Plot"] as! String
                    self.runtime = dict["Runtime"] as! String
                    self.media = "No Media"
                } else {
                    self.title = "No movie Found"
                }
                
            } catch {
                print("unable to convert to dictionary")
                
            }
        }
    }
    
    // this is from rpc server
    init (jsonString: String, fromRPCServer:Bool) {
        self.title = ""
        self.rated = ""
        self.released = ""
        self.actors = "-"
        self.genre = ""
        self.plot = "-"
        self.year = ""
        self.runtime = ""
        self.media = "No Media"
        self.id = NSUUID().UUIDString
        
        if fromRPCServer == true {
            if let data: NSData = jsonString.dataUsingEncoding(NSUTF8StringEncoding) {
                do{
                    let dict = try NSJSONSerialization.JSONObjectWithData(data, options:.MutableContainers) as! [String:AnyObject]
                        self.title = dict["Title"] as! String
                        self.year = dict["Year"] as! String
                        self.rated = dict["Rated"] as! String
                        self.released = dict["Released"] as! String
                        self.genre = dict["Genre"] as! String
                        self.actors = dict["Actors"] as! String
                        self.plot = dict["Plot"] as! String
                        self.runtime = dict["Runtime"] as! String
                        self.media = dict["Filename"] as! String
                    
                } catch {
                    print("unable to convert to dictionary")
                    
                }
            }
        }
    }
    
    
    // In this case id is present meaning movie is present so get the media and set it
    init(id:String, title:String, year:String, rated:String, released:String, runtime:String, genre:String, actors:String, plot:String, media:String) {
        self.title = title
        self.rated = rated
        self.released = released
        self.actors = actors
        self.genre = genre
        self.plot = plot
        self.year = year
        self.runtime = runtime
        self.id = id
        self.media = media
    }
    
    func toJsonString() -> String {
        var jsonStr = "";
        let dict = ["Title": self.title, "Year": year, "Rated": self.rated, "Released": self.released, "Genre": self.genre, "Actors": self.actors,"Plot": self.plot, "Runtime": self.runtime, "Media":self.media]
        do {
            let jsonData = try NSJSONSerialization.dataWithJSONObject(dict, options: NSJSONWritingOptions(rawValue: 0))
            // here "jsonData" is the dictionary encoded in JSON data
            jsonStr = NSString(data: jsonData, encoding: NSUTF8StringEncoding)! as String
        } catch let error as NSError {
            print(error)
        }
        return jsonStr
    }
    
    // This is to manually create a movie so set to default media
    init (title: String, year: String, rated: String, released: String, runtime:String, genre: String, actors: String, plot: String) {
        self.id = NSUUID().UUIDString
        self.title = title
        self.year = year
        self.rated = rated
        self.released = released
        self.runtime = runtime
        self.genre = genre
        self.actors = actors
        self.plot = plot
        self.media = "No Media"
    }
    
    func toDict() -> [String: String]{
        var  movieDict:[String: String] = [String: String]()
        movieDict["Title"] = self.title
        movieDict["Rated"] = self.rated
        movieDict["Released"] = self.released
        movieDict["Actors"] =  self.actors
        movieDict["Genre"] = self.genre
            movieDict["Plot"] = self.plot
        movieDict["Year"] = self.year
        movieDict["Runtime"] = self.runtime
        movieDict["Id"] = self.id
        movieDict["Media"] = self.media
        return movieDict
    }
    
    func getMedia() -> String {
        return self.media
    }
    
    func setMedia(media:String) {
        self.media = media
    }
    
    func getId() -> String {
        return self.id
    }
    
    func setId(id:String) {
        self.id = id
    }
    
    func getRuntime() -> String {
        return self.runtime
    }
    
    func setRuntime(runtime:String) {
        self.runtime = runtime
    }
    
    func getGenre() -> String {
        return self.genre
    }
    
    func getTitle() -> String {
        return self.title
    }
    
    func getPlot() -> String {
        return self.plot
    }
    
    func getYear() -> String {
        return self.year
    }
    
    func getRated() -> String  {
        return self.rated
    }
    
    func getReleased() -> String  {
        return self.released
    }
    
    func getActors() -> String  {
        return self.actors
    }
    func setGenre(genre: String) {
        self.genre = genre
    }
    
    func setTitle(title: String){
        self.title = title
    }
    
    func setPlot(plot: String){
        self.plot = plot
    }
    
    func setYear(year: String){
        self.year = year
    }
    
    func setRated(rated: String){
        self.rated = rated
    }
    
    func setReleased(released: String) {
        self.released = released
    }
    
    func setActors(actors: String) {
        self.actors = actors
    }
}
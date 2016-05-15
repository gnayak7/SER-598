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
* @version February 2016
*/

import Foundation

// This is movie library class
class MovieLibrary {
    var models: Array<Model>
    var movies: Dictionary<String, Movie>
    
    init(jsonObjectsArray: Array<Dictionary<String,AnyObject>>) {
        // create new model array
        // create new movies dictionary
        self.models = Array<Model>()
        self.movies = Dictionary<String, Movie>()
        //        print(data)
        do {
            for jsonObject in jsonObjectsArray {
                let dict = jsonObject as NSDictionary
                let serializedJsonData = try NSJSONSerialization.dataWithJSONObject(dict, options: NSJSONWritingOptions.PrettyPrinted)
                let jsonNSString = NSString(data: serializedJsonData, encoding: NSUTF8StringEncoding)
                let jsonMovieString = jsonNSString as! String
                let movie = Movie(jsonString: jsonMovieString)
                
                self.addOrUpdateModelAndMovies(movie)
                
            }
        } catch {
            
        }
    }
    
    func getMovie(groupPosition: Int, childPosition: Int) -> Movie {
        let movieName: String = self.models[groupPosition].getMovieNameAt(childPosition)
        return movies[movieName]!
      
    }
    
    func addOrUpdateModelAndMovies(movie:Movie) {
        var curModel: Model?
        
        for model in models {
            if movie.getGenre() == model.getGenre() {
                curModel = model
            }
        }
        
        if curModel == nil {
            let newModel = Model(genre: movie.genre, name: movie.getTitle())
            models.append(newModel)
        } else {
            curModel!.add(movie.getTitle())
        }
        
        movies.updateValue(movie, forKey: movie.getTitle())
    }
    
    func getGroupCount() -> Int {
        return self.models.count
    }
    
    func getChildrenCount(groupPosition: Int) -> Int {
        return self.models[groupPosition].getCount()
    }
    
    func deleteMovie(groupPosition:Int, childPosition:Int) {
        
        let movieName = models[groupPosition].getMovieNameAt(childPosition)
        movies.removeValueForKey(movieName)
        
        models[groupPosition].delete(childPosition)
        
        if (models[groupPosition].getCount() == 0) {
            models.removeAtIndex(groupPosition)
        }
    }
    
    func addMovie(movie: Movie) {
        let movieName = movie.getTitle()
        let movieGenre = movie.getGenre()
        if movieName != "" && movieGenre != "" {
            addOrUpdateModelAndMovies(movie)
        }
    }
    
    func editMovie(groupPostion:Int, childPostion:Int, editedMovie: Movie) {
        let oldMovie = getMovie(groupPostion, childPosition: childPostion)
        if (oldMovie.getGenre() == editedMovie.getGenre()) {
            oldMovie.setTitle(editedMovie.getTitle())
            oldMovie.setYear(editedMovie.getYear())
            oldMovie.setRated(editedMovie.getRated())
            oldMovie.setReleased(editedMovie.getReleased())
            oldMovie.setActors(editedMovie.getActors())
            oldMovie.setPlot(editedMovie.getPlot())
        } else {
            deleteMovie(groupPostion, childPosition: childPostion)
            addMovie(editedMovie)
        }
    }
    
    func deleteMovie(movie:Movie) {
        let (groupPostition, childPosition) = searchMovie(movie)
        if (groupPostition != -1 && childPosition != -1) {
            deleteMovie(groupPostition, childPosition: childPosition)
        }
    }
    
    func searchMovie(movie:Movie) -> (Int, Int) {
        let movieName = movie.getTitle()
        let modelSize = getGroupCount()
        for (var i = 0; i < modelSize; i++) {
            let moviesCount = getChildrenCount(i)
            let arr = models[i].movieNames
            for (var j = 0; j < moviesCount; j++) {
                if (movieName == arr[j]) {
                    return (i,j)
                }
            }
        }
        return (-1,-1)
    }
    
    func getGenreAt(groupPosition:Int) -> String {
        return self.models[groupPosition].getGenre()
    }
    
    func getChildAt(groupPosition: Int, childPosition: Int) -> String {
        return self.models[groupPosition].getMovieNameAt(childPosition)
    }
    
    // will accept an array of json objects and convert them to models and movies
}
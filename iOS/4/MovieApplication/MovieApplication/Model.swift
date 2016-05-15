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


// This class is a model class to hold the genre and related movie titles
class Model{
    var genre: String
    var movieNames: Array<String>
    
    init(genre: String, name: String) {
        self.genre = genre
        self.movieNames = Array<String>()
        self.add(name)
    }
    
    func add(name: String) {
        self.movieNames.append(name)
    }
    
    func getGenre() -> String {
        return self.genre
    }
    
    func getCount() -> Int {
        return movieNames.count
    }
    
    func delete(position: Int) {
        self.movieNames.removeAtIndex(position)
    }
    
    func getMovieNameAt(position: Int) -> String {
        return self.movieNames[position]
    }
}

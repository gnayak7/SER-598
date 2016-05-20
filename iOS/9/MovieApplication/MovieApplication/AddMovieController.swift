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
import UIKit
import CoreData


protocol AddMovieControllerDelegate {
    func doSomething(addMovieController:AddMovieController, movie:Movie)
}

// This class is used to add a new movie
class AddMovieController: UIViewController {
    @IBOutlet weak var textFieldTitle: UITextField!
    @IBOutlet weak var textFieldYear: UITextField!
    @IBOutlet weak var textFieldRated: UITextField!
    @IBOutlet weak var textFieldReleased: UITextField!
    @IBOutlet weak var textFieldGenre: UITextField!
    @IBOutlet weak var textFieldRuntime: UITextField!
    @IBOutlet weak var textViewActors: UITextView!
    @IBOutlet weak var textViewPlot: UITextView!
    
    var delegate:AddMovieControllerDelegate?
    
    @IBAction func saveClicked(sender: UIButton) {
        let movie:Movie = Movie(title: textFieldTitle.text!, year: textFieldYear.text!, rated: textFieldRated.text!, released: textFieldReleased.text!, runtime: textFieldRuntime.text!, genre: textFieldGenre.text!, actors: textViewActors.text, plot: textViewPlot.text)
        delegate?.doSomething(self, movie: movie)
    }
    
    @IBAction func searchClicked(sender: UIButton) {
        let movieName = textFieldTitle.text
        let movieTitle = movieName!.stringByReplacingOccurrencesOfString(" ", withString: "+", options: NSStringCompareOptions.LiteralSearch, range: nil)
        let url: String = "https://www.omdbapi.com/?t=" + movieTitle + "&y=&plot=short&r=json"
        getMovieData(url)
    }
    
    
    func getMovieData(urlString: String) {
        let movieNSURL = NSURL(string: urlString)
        let movieTask = NSURLSession.sharedSession().dataTaskWithURL(movieNSURL!) {(jsonObjectData, response, error) in
            dispatch_async(dispatch_get_main_queue(),{
                let jsonData = NSString(data:jsonObjectData!, encoding: NSUTF8StringEncoding) as? String
                let movie = Movie(jsonString: jsonData! as String)
                self.textFieldTitle.text  = movie.getTitle()
                self.textFieldYear.text = movie.getYear()
                self.textFieldRated.text = movie.getRated()
                self.textFieldReleased.text = movie.getReleased()
                self.textFieldGenre.text = movie.getGenre()
                self.textFieldRuntime.text = movie.getRuntime()
                self.textViewActors.text = movie.getActors()
                self.textViewPlot.text = movie.getPlot()
            })
        }
        movieTask.resume()
    }
}

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
import CoreData
protocol EditMovieControllerDelegate {
    func editMovie(editMovieController: EditMovieController, movie: Movie)
}


// This class is a controller for displaying movie details and also to edit movies.
class EditMovieController: UIViewController {
    
    var movieId: String?
    var delegate: EditMovieControllerDelegate?
    var media:String?
    
    @IBOutlet weak var textFieldTitle: UITextField!
    @IBOutlet weak var textFieldYear: UITextField!
    @IBOutlet weak var textFieldRated: UITextField!
    @IBOutlet weak var textFieldReleased: UITextField!
    @IBOutlet weak var textFieldGenre: UITextField!
    @IBOutlet weak var textViewPlot: UITextView!
    @IBOutlet weak var textViewActors: UITextView!
    @IBOutlet weak var textFieldRuntime: UITextField!
    
    @IBAction func textFieldEditing(sender: UITextField) {
        let datePickerView: UIDatePicker = UIDatePicker()
        datePickerView.datePickerMode = UIDatePickerMode.Date
        sender.inputView = datePickerView
        datePickerView.addTarget(self,action: Selector("datePickerValueChanged:"),forControlEvents: UIControlEvents.ValueChanged )
    }
    
    func datePickerValueChanged(sender: UIDatePicker){
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "d MMM YYYY"
        textFieldReleased.text = dateFormatter.stringFromDate(sender.date)
    }
    
    @IBAction func playVideo(sender: UIButton) {
        if media != "No Media" {
            let secondViewController = self.storyboard!.instantiateViewControllerWithIdentifier("VideoPlayer") as! VideoPlayer
            secondViewController.media = media
            self.navigationController!.pushViewController(secondViewController, animated: true)
        } else {
            print("No Media")
        }
    }
    
    
    @IBAction func onSaveClick(sender: UIButton) {
        // get all the values from the fields and return back as movie with same id
        
        let movie = Movie(id: movieId!, title: textFieldTitle.text!, year: textFieldYear.text!, rated: textFieldRated.text!, released: textFieldReleased.text!, runtime: textFieldRuntime.text!, genre: textFieldGenre.text!, actors: textViewActors.text, plot: textViewPlot.text, media:media!)
        delegate?.editMovie(self, movie: movie)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let selectRequest = NSFetchRequest(entityName: "MovieDatabaseTable")
        selectRequest.predicate = NSPredicate(format: "id == %@", movieId!)
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        
        do{
            let results = try managedContext.executeFetchRequest(selectRequest)
            if results.count > 0 {
                textFieldTitle.text =  results[0].valueForKey("title") as? String
                textFieldYear.text =  results[0].valueForKey("year") as? String
                textFieldReleased.text =  results[0].valueForKey("released") as? String
                textFieldRuntime.text =  results[0].valueForKey("runtime") as? String
                textFieldGenre.text =  results[0].valueForKey("genre") as? String
                textViewActors.text =  results[0].valueForKey("actors") as? String
                textViewPlot.text =  results[0].valueForKey("plot") as? String
                media = results[0].valueForKey("media") as? String
                
                textFieldRated.text = (results[0].valueForKey("takes") as? NSManagedObject)?.valueForKey("rated") as? String
            }
        } catch {
            NSLog("Error selecting student ")
        }
    }
    
}

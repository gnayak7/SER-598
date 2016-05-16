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

import UIKit

protocol AddOrEditViewControllerDelegate {
    func passMovieFromEditViewToMovieController(addOrEditMovieController: AddOrEditViewController, groupPosition:Int, childPosition:Int, newMovie: Movie)
}


// This class is a controlle for displaying movie details and also to add new movies.
class AddOrEditViewController: UIViewController {
    
    var movieName: String?
    var delegate: AddOrEditViewControllerDelegate?
    var groupPosition:Int?
    var childPosition: Int?
    var movieCache: [String:Movie]?
    
    @IBOutlet weak var textFieldTitle: UITextField!
    @IBOutlet weak var textFieldYear: UITextField!
    @IBOutlet weak var textFieldRated: UITextField!
    @IBOutlet weak var textFieldReleased: UITextField!
    @IBOutlet weak var textFieldGenre: UITextField!
    @IBOutlet weak var textViewPlot: UITextView!
    @IBOutlet weak var textViewActors: UITextView!
    
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
    
    @IBAction func onSaveClick(sender: UIButton) {
        //        let movie: Movie = Movie(a,b,c,d,e,f,g)
        let movieTitle = textFieldTitle.text
        let movieYear = textFieldYear.text
        let movieRated = textFieldRated.text
        let movieReleased = textFieldReleased.text
        let movieGenre = textFieldGenre.text
        let movieActors = textViewActors.text
        let moviePlot = textViewPlot.text
        let editedMovie = Movie(name: movieTitle!,year: movieYear!,rated: movieReleased!, released: movieRated!, genre: movieGenre!, actors: movieActors, plot: moviePlot)
        if groupPosition != nil {
            delegate?.passMovieFromEditViewToMovieController(self, groupPosition: groupPosition!, childPosition: childPosition!, newMovie: editedMovie)
        } else {
            delegate?.passMovieFromEditViewToMovieController(self, groupPosition: -1,childPosition: -1, newMovie: editedMovie)
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let _ = self.movieName {
            _ = RPCRequestAndResponse(methodInformation: "get",params: [self.movieName!],id: "2", doSomething: {
                response in
                let responseValue = response["result"] as! [String:AnyObject]
                do {
                    let serializedJsonData = try NSJSONSerialization.dataWithJSONObject(responseValue, options: NSJSONWritingOptions.PrettyPrinted)
                    let jsonNSString = NSString(data: serializedJsonData, encoding: NSUTF8StringEncoding)
                    let jsonMovieString = jsonNSString as! String
                    let movieFromServer = Movie(jsonString: jsonMovieString)
                    //                self.movieCache!.updateValue(movieFromServer, forKey: movieFromServer.getTitle())
                    self.populateTextFields(movieFromServer)
                } catch {
                    
                }
            })
        }
        
    }
    
    func populateTextFields(movie:Movie?) {
        textFieldTitle.text = movie?.getTitle()
        textFieldYear.text = movie?.getYear()
        textFieldRated.text = movie?.getRated()
        textFieldReleased.text = movie?.getReleased()
        textFieldGenre.text = movie?.getGenre()
        textViewActors.text = movie?.getActors()
        textViewPlot.text = movie?.getPlot()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /*
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    // Get the new view controller using segue.destinationViewController.
    // Pass the selected object to the new view controller.
    }
    */
    
}

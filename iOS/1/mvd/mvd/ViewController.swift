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

import UIKit

/*
This is the main User Interface Class to make an asynchronous HTTP Request and get the JSON
Response and build a container class to extract all the fields and display it on the screen.
*/

class ViewController: UIViewController {
    
    
    @IBOutlet weak var main_page_id_title: UILabel!
    @IBOutlet weak var main_page_id_year: UILabel!
    @IBOutlet weak var main_page_id_rated: UILabel!
    @IBOutlet weak var main_page_id_released: UILabel!
    @IBOutlet weak var main_page_id_genre: UILabel!
    @IBOutlet weak var main_page_id_actors: UILabel!
    @IBOutlet weak var main_page_id_plot: UILabel!
    @IBOutlet weak var main_page_id_runtime: UILabel!

    @IBAction func butonClicked(sender: UIButton) {
        let url: String = "https://www.omdbapi.com/?t=Frozen&y=&plot=short&r=json"
        getMovieData(url)
    }
    
    func getMovieData(urlString: String) {
        let movieNSURL = NSURL(string: urlString)
        let movieTask = NSURLSession.sharedSession().dataTaskWithURL(movieNSURL!) {(jsonObjectData, response, error) in
            dispatch_async(dispatch_get_main_queue(),{
                let jsonData = NSString(data:jsonObjectData!, encoding: NSUTF8StringEncoding) as? String
                let description = Movie(jsonStr: jsonData! as String)
                self.main_page_id_title.text = "Title: \(description.getTitle())"
                self.main_page_id_year.text = "Year: \(description.getYear())"
                self.main_page_id_rated.text = "Rated: \(description.getRated())"
                self.main_page_id_runtime.text = "Runtime: \(description.getRuntime())"
                self.main_page_id_released.text = "Released: \(description.getReleased())"
                self.main_page_id_genre.text = "Genre: \(description.getGenre())"
                self.main_page_id_actors.text = "Actors: \(description.getActors())"
                self.main_page_id_plot.text = "Plot: \(description.getPlot())"
                print(description.toJsonString())
            })
        }
        movieTask.resume()
    }

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

/*
References:
1) developer.apple.com
2) pooh.poly.asu.edu/Mobile/Assigns/LabsSpr2016/Assign1/assign1.html
*/
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
* Purpose: Assignment 2 Submission
* This example demonstrates different states of an iOS application. Additonally
* it prints out a log statement on the console when the application enters
* each of the state.
*
* SER 598 Mobile Applications
* @author gnayak2@asu.edu
* @version January 2016
*/

import UIKit


// This is the starting point of the application. This is the first class that is executed after the application launches.
// It has an alert window which is popped up when the button is clicked. It closes the window when 'ok' button is clicked.

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // Called when button is clicked. Alert window disappears when 'ok' button is clicked.
    @IBAction func onClickButton(sender: UIButton) {
        let alertController = UIAlertController(title: "hey! :)", message: "I am alert", preferredStyle: .Alert)
        let defaultAction = UIAlertAction(title:"ok", style: .Default, handler:nil)
        alertController.addAction(defaultAction)
        self.presentViewController(alertController, animated: true, completion: nil)
    }

}


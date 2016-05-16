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

//This class is a controller for Table View
class MovieListTableViewController: UITableViewController, AddOrEditViewControllerDelegate{
    
    var movieLibrary: MovieLibrary?
    
    @IBAction func refreshMovieList(sender: UIRefreshControl) {
        _ = RPCRequestAndResponse(methodInformation: "resetFromJsonFile",params: [""],id: "2", doSomething: { _ in self.callServer() })
        sender.endRefreshing()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NSLog("application : ", "started")
        callServer()
    }
    
    func callServer() {
        _ = RPCRequestAndResponse(methodInformation: "getModelInformation",params: [""],id: "2", doSomething: {
            response in
            let responseValue = response["result"] as! [String:AnyObject]
            var arrayOfModels = [Model]()
            for (key,value) in responseValue {
                let newModel = Model(genre: key , movieNamesArray: value as! [String])
                arrayOfModels.append(newModel)
            }
            self.movieLibrary = MovieLibrary(models: arrayOfModels)
            self.tableView.reloadData()})
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        if let count = self.movieLibrary?.getGroupCount() {
            return count
        } else {
            return 0
        }
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let count = self.movieLibrary?.getChildrenCount(section) {
            return count
        } else {
            return 0
        }
    }
    
    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return self.movieLibrary!.getGenreAt(section)
    }

    private struct Storyboard {
        static let CELL_REUSE_IDENTIFIER = "MovieCell"
    }
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCellWithIdentifier(Storyboard.CELL_REUSE_IDENTIFIER, forIndexPath: indexPath) as! MovieTableViewCell
        
        cell.cellName = movieLibrary!.getMovieName(indexPath.section, childPosition: indexPath.row)
        return cell
    }
   
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }

    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == UITableViewCellEditingStyle.Delete {
             _ = RPCRequestAndResponse(methodInformation: "remove",params: [movieLibrary!.getMovieName(indexPath.section, childPosition: indexPath.row)],id: "2", doSomething: {_ in })
            let count = movieLibrary?.getChildrenCount(indexPath.section)
            movieLibrary?.deleteMovie(indexPath.section, childPosition: indexPath.row)
            
            if count > 1 {
                tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
            } else {
                tableView.deleteSections(NSIndexSet(index: indexPath.section), withRowAnimation: .Fade)
            }
            tableView.reloadData()
   
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation
    private struct SegueIdentifier {
        static let editORAdd = "EditOrAdd"
        static let newMovie = "NewMovie"
    }
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == SegueIdentifier.editORAdd {
            let destinationViewController: AddOrEditViewController = segue.destinationViewController as! AddOrEditViewController
            let indexPath = self.tableView.indexPathForSelectedRow
            
            destinationViewController.movieName = movieLibrary?.getMovieName(indexPath!.section, childPosition: indexPath!.row)
            destinationViewController.groupPosition = indexPath!.section
            destinationViewController.childPosition = indexPath!.row
            destinationViewController.delegate = self
        }
        else if segue.identifier == SegueIdentifier.newMovie {
            let destinationViewController: AddOrEditViewController = segue.destinationViewController as! AddOrEditViewController
            destinationViewController.delegate = self
        }
    }

    func passMovieFromEditViewToMovieController(addOrEditViewController: AddOrEditViewController, groupPosition: Int, childPosition: Int, newMovie: Movie) {
        if groupPosition != -1 {            
            // if movie genre and title are same
            let movieName =  movieLibrary?.getMovieName(groupPosition, childPosition: childPosition)
            let movieGenre = movieLibrary?.models[groupPosition].getGenre()
            //else if they are different
            
            if movieName == newMovie.getTitle() && movieGenre == newMovie.getGenre() {
                _ = RPCRequestAndResponse(methodInformation: "update",params: newMovie.toJsonString(),id: "2", doSomething: { _ in })
            } else {
                var arrayOfParams = [String]()
                
                let oldMovie = Movie(name: movieName!, year: "", rated: "", released: "", genre: movieGenre!, actors: "", plot: "")
                arrayOfParams.append(oldMovie.toJsonString())
                arrayOfParams.append(newMovie.toJsonString())
                movieLibrary!.deleteAndAdd(groupPosition, childPosition: childPosition, newMovie: newMovie)
                _ = RPCRequestAndResponse(methodInformation: "deleteAndAdd",params: arrayOfParams,id: "2", doSomething: { _ in self.tableView.reloadData()})
            }
            
            self.tableView.reloadData()
            addOrEditViewController.navigationController?.popViewControllerAnimated(true)
            
        } else {
            _ = RPCRequestAndResponse(methodInformation: "add",params: newMovie.toJsonString(),id: "2", doSomething: { _ in })
            movieLibrary?.addMovie(newMovie)
            self.tableView.reloadData()
            addOrEditViewController.navigationController?.popViewControllerAnimated(true)
        }
    }

}

/* 
References:

1) pooh.poly.asu.edu
2) stackoverflow.com
3) developer.apple.com/library/ios/referencelibrary/GettingStarted/DevelopiOSAppsSwift/Lesson7.html
*/
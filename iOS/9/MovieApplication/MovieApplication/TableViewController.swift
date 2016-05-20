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


// This is the table view controller
class TableViewController: UITableViewController, AddMovieControllerDelegate, EditMovieControllerDelegate, UISearchResultsUpdating {
    var moviesList = [NSManagedObject]()
    var filteredMoviesList = [NSManagedObject]()
    var serverCallId = 0
    
    var searchController = UISearchController(searchResultsController: nil)
    
    @IBAction func refreshMoviesList(sender: UIRefreshControl) {
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        for movie in moviesList {
            managedContext.deleteObject(movie)
        }
        moviesList.removeAll()
        initialize()
        tableView.reloadData()
        sender.endRefreshing()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.searchController = UISearchController(searchResultsController:  nil)
        searchController.searchResultsUpdater = self
        searchController.dimsBackgroundDuringPresentation = true
        self.searchController.hidesNavigationBarDuringPresentation = false
        definesPresentationContext = true
        self.navigationItem.titleView = searchController.searchBar
        initialize()
    }
    
    func initialize() {
        _ = RPCRequestAndResponse(methodInformation: "getTitles",params: [""],id: "2", doSomething: {
            response in
            let movieTitles = response["result"] as! [String]
            print("initialize getting called")
            for title in movieTitles {
                let movieExistsInDatabase = self.checkIfMovieExistsInDatabase(title)
                print(movieExistsInDatabase)
                if !movieExistsInDatabase {
                    self.serverCallId = self.serverCallId + 1
                    _ = RPCRequestAndResponse(methodInformation: "get",params: title,id: "\(self.serverCallId)", doSomething: {
                            response in
                                let responseMovieJson = response["result"] as! NSDictionary
                                do {
                                        let serializedJsonData = try NSJSONSerialization.dataWithJSONObject(responseMovieJson, options: NSJSONWritingOptions.PrettyPrinted)
                                        let jsonNSString = NSString(data: serializedJsonData, encoding: NSUTF8StringEncoding)
                                        let jsonMovieString = jsonNSString as! String
                                        let fetchedMovie = Movie(jsonString: jsonMovieString, fromRPCServer: true)
                                        self.addMovieToDatabase(fetchedMovie)
                                        self.tableView.reloadData()
                                } catch {
                                        print ("In catch")
                                }
                    })
                    
                }
            }
        })
    }
    
    func checkIfMovieExistsInDatabase(movieTitle: String) -> Bool{
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        
        let movieTableQuery = NSFetchRequest(entityName: "MovieDatabaseTable")
        movieTableQuery.predicate = NSPredicate(format: "title == %@", movieTitle)
        
        //var ratedManagedObject:NSManagedObject?
        
        do {
            let storedMovie = try managedContext.executeFetchRequest(movieTableQuery) as! [NSManagedObject]
            if storedMovie.count > 0 {
                return true
            }
        } catch {
            print("Error adding the rating to database")
        }
        return false
    }
    
    override func viewWillAppear(animated: Bool) {
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        let fetchRequest = NSFetchRequest(entityName: "MovieDatabaseTable")
        
        do {
            let results = try managedContext.executeFetchRequest(fetchRequest)
            moviesList = results as! [NSManagedObject]
        } catch {
            print("Error")
        }
    }
    
    func filterContentForSearchText(searchText: String, scope: String = "All") {
        filteredMoviesList = moviesList.filter { moiveNSManagedObject in
            return moiveNSManagedObject.valueForKey("title")!.lowercaseString.containsString(searchText.lowercaseString)
        }
        
        tableView.reloadData()
    }
    
    func updateSearchResultsForSearchController(searchController: UISearchController) {
        filterContentForSearchText(searchController.searchBar.text!)
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Table view data source
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if searchController.active && searchController.searchBar.text != "" {
            return filteredMoviesList.count
        }
        return  moviesList.count
    }
    
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as UITableViewCell
        let movieNSManagedObject: NSManagedObject
        if searchController.active && searchController.searchBar.text != "" {
            movieNSManagedObject = filteredMoviesList[indexPath.row]
        } else {
            movieNSManagedObject = moviesList[indexPath.row]
        }
        cell.textLabel?.text = movieNSManagedObject.valueForKey("title") as? String
        return cell
    }
    
    
    
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete from database if succeeds then
            let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
            let managedContext = appDelegate.managedObjectContext
            managedContext.deleteObject(moviesList[indexPath.row] as NSManagedObject)
            moviesList.removeAtIndex(indexPath.row)
            do {
                try managedContext.save()
            }catch {
                
            }
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
            self.tableView.reloadData()
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "addMovieSegue" {
            let destinationViewController: AddMovieController = segue.destinationViewController as! AddMovieController
            destinationViewController.delegate = self
        } else if segue.identifier == "editMovieSegue" {
            let destinationViewController: EditMovieController = segue.destinationViewController as! EditMovieController
            let indexPath = self.tableView.indexPathForSelectedRow
            let movieNSManagedObject = moviesList[indexPath!.row]
            let movieId = movieNSManagedObject.valueForKey("id") as! String
            destinationViewController.movieId = movieId
            destinationViewController.delegate = self
        }
    }
    
    func doSomething(addMovieController:AddMovieController, movie:Movie) {
        addMovieToDatabase(movie)
        tableView.reloadData()
        addMovieController.navigationController?.popViewControllerAnimated(true)
    }
    
    func editMovie(editMovieController: EditMovieController, movie: Movie) {
        editDatabase(movie)
        tableView.reloadData()
        editMovieController.navigationController?.popViewControllerAnimated(true)
    }
    
    func addMovieToDatabase(movie:Movie) {
        
        
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        
        
        // Add the rated to database if not present else just point it to that row
        let selectRequestRated = NSFetchRequest(entityName: "RatedTable")
        selectRequestRated.predicate = NSPredicate(format: "rated == %@", movie.getRated())
        
        var ratedManagedObject:NSManagedObject?
        
        do{
            let resultsRated = try managedContext.executeFetchRequest(selectRequestRated) as! [NSManagedObject]
            if resultsRated.count <= 0 {
                // This means the rating is not present in the database. Add it to the database
                let entityRated = NSEntityDescription.entityForName("RatedTable", inManagedObjectContext: managedContext)
                ratedManagedObject = NSManagedObject(entity: entityRated!, insertIntoManagedObjectContext: managedContext)
                ratedManagedObject!.setValue(movie.getRated(), forKey:"rated")
                do{
                    try managedContext.save()
                } catch let error as NSError{
                    print("Error adding entity for new rating \(movie.getRated()). Error: \(error)")
                }
            } else {
                ratedManagedObject = resultsRated[0]
            }
            
            // Add the Movie to the database
            let entity = NSEntityDescription.entityForName("MovieDatabaseTable", inManagedObjectContext: managedContext)
            let item = NSManagedObject(entity: entity!, insertIntoManagedObjectContext: managedContext)
            
            item.setValue(movie.getId(), forKey: "id")
            item.setValue(movie.getTitle(), forKey: "title")
            item.setValue(movie.getYear(), forKey: "year")
            item.setValue(movie.getReleased(), forKey: "released")
            item.setValue(movie.getRuntime(), forKey: "runtime")
            item.setValue(movie.getGenre(), forKey: "genre")
            item.setValue(movie.getActors(), forKey: "actors")
            item.setValue(movie.getPlot(), forKey:"plot")
            item.setValue(movie.getMedia(), forKey:"media")
            
            item.setValue(ratedManagedObject, forKey: "takes")
            
            do {
                try managedContext.save()
                moviesList.append(item)
                
            } catch {
                print("Eror adding movie to database")
            }
            
        } catch {
            print("Error getting the rating for the movie")
        }
    }
    
    
    func editDatabase(movie:Movie) {
        
        
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let managedContext = appDelegate.managedObjectContext
        
        let selectRequestRated = NSFetchRequest(entityName: "RatedTable")
        selectRequestRated.predicate = NSPredicate(format: "rated == %@", movie.getRated())
        
        var ratedManagedObject:NSManagedObject?
        do {
            let resultsRated = try managedContext.executeFetchRequest(selectRequestRated) as! [NSManagedObject]
            if resultsRated.count <= 0 {
                // This means the rating is not present in the database. Add it to the database
                let entityRated = NSEntityDescription.entityForName("RatedTable", inManagedObjectContext: managedContext)
                ratedManagedObject = NSManagedObject(entity: entityRated!, insertIntoManagedObjectContext: managedContext)
                ratedManagedObject!.setValue(movie.getRated(), forKey:"rated")
                do{
                    try managedContext.save()
                } catch let error as NSError{
                    print("Error adding entity for new rating \(movie.getRated()). Error: \(error)")
                }
            } else {
                ratedManagedObject = resultsRated[0]
            }
        } catch {
            print("Error adding the rating to database")
        }
        
        // get the managed object
        let selectRequest = NSFetchRequest(entityName: "MovieDatabaseTable")
        selectRequest.predicate = NSPredicate(format: "id == %@", movie.getId())
        
        do{
            let results = try managedContext.executeFetchRequest(selectRequest) as! [NSManagedObject]
            if results.count > 0 {
                let mangedObject = results[0]
                mangedObject.setValue(movie.getTitle(), forKey: "title")
                mangedObject.setValue(movie.getYear(), forKey: "year")
                mangedObject.setValue(movie.getReleased(), forKey: "released")
                mangedObject.setValue(movie.getRuntime(), forKey: "runtime")
                mangedObject.setValue(movie.getGenre(), forKey: "genre")
                mangedObject.setValue(movie.getActors(), forKey: "actors")
                mangedObject.setValue(movie.getPlot(), forKey: "plot")
                mangedObject.setValue(movie.getMedia(), forKey:"media")
                
                mangedObject.setValue(ratedManagedObject, forKey: "takes")
                try managedContext.save()
            }
        } catch {
            NSLog("Error selecting student ")
        }
    }
}

/*
References.
1. http://pooh.poly.asu.edu
2. https://www.raywenderlich.com/115695/getting-started-with-core-data-tutorial
3. https://developer.apple.com/library/mac/documentation/Cocoa/Conceptual/CoreData/HowManagedObjectsarerelated.html
*/

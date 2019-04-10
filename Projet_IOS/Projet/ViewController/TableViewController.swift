//
//  TableViewController.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/5/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import UIKit
import CoreData

class TableViewController : UITableViewController,NSFetchedResultsControllerDelegate{
    
    var managedObjectContext:NSManagedObjectContext? = nil
    var container = NSPersistentContainer(name: "Model")
    
    var nomJoueur:String = ""
    var score:Int = 0
    var date:Date = Date()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.rightBarButtonItem = editButtonItem
        
        //Mark : - Core Data Stack
       container.loadPersistentStores(completionHandler:
            { (storeDescription, error) in
                if let error = error as NSError? {
                        print ("unresolved error \(error)")
                    }
            }
        )
 
        
    }
    //Mark : - Core Data Saving support
    func saveContext(){
        if container.viewContext.hasChanges{
            do{
                try container.viewContext.save()
            } catch {
                print("An error occured while saving : \(error)")
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK : - Table View
    override func numberOfSections(in tableView: UITableView) -> Int{
        return fetchedResultsController.sections?.count ?? 0
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let sectionInfo = fetchedResultsController.sections![section]
        return sectionInfo.numberOfObjects
    }
    
    override func tableView(_ tableView: UITableView,cellForRowAt indexPath:IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell",for: indexPath)
        let score = fetchedResultsController.object(at: indexPath)
        
        configureCell(cell, avecScore: score)
        return cell
    }

    override func tableView(_ tableView:UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath:IndexPath){
        
        if editingStyle == .delete{
            let context = fetchedResultsController.managedObjectContext
            context.delete(fetchedResultsController.object(at : indexPath))
            
            do{
                try context.save()
            } catch {
                let nserror = error as NSError
                fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
            }
        }
        
    }
    
    func configureCell(_ cell : UITableViewCell, avecScore score:Score?){
        if(score != nil){
            
            let format = DateFormatter()
            format.dateFormat = "dd-MM-yyyy"
            
            cell.textLabel!.text = String(score!.score)
            cell.detailTextLabel?.text = score!.nomJoueur! + " "+format.string(from: score!.date!)
        }
    }
    
    // MARK : Fetched results controller
    var fetchedResultsController: NSFetchedResultsController<Score> {
    
        if _fetchedResultsController != nil {
            return _fetchedResultsController!
        }
        
        let fetchRequest: NSFetchRequest<Score> = Score.fetchRequest()
        
        // Set the batch size to a suitable number.
        fetchRequest.fetchBatchSize = 20
        
        // Edit the sort key as appropriate.
        let sortDescriptor = NSSortDescriptor(key: "score", ascending: false)
        
        fetchRequest.sortDescriptors = [sortDescriptor]
        
        // Edit the section name key path and cache name if appropriate.
        // nil for section name key path means "no sections".
        let aFetchedResultsController = NSFetchedResultsController(fetchRequest: fetchRequest, managedObjectContext: container.viewContext, sectionNameKeyPath: nil, cacheName: nil)
        
        aFetchedResultsController.delegate = self
        _fetchedResultsController = aFetchedResultsController
        
        do {
            try _fetchedResultsController!.performFetch()
        } catch {
            // Replace this implementation with code to handle the error appropriately.
            // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            let nserror = error as NSError
            fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
        }
        
        return _fetchedResultsController!
    }
    
    var _fetchedResultsController: NSFetchedResultsController<Score>? = nil
    
    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.beginUpdates()
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange sectionInfo: NSFetchedResultsSectionInfo, atSectionIndex sectionIndex: Int, for type: NSFetchedResultsChangeType) {
        switch type {
            case .insert:
                tableView.insertSections(IndexSet(integer: sectionIndex), with: .fade)
            case .delete:
                tableView.deleteSections(IndexSet(integer: sectionIndex), with: .fade)
            default:
                return
        }
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        switch type {
            case .insert:
                tableView.insertRows(at: [newIndexPath!], with: .fade)
            case .delete:
                tableView.deleteRows(at: [indexPath!], with: .fade)
            case .update:
                configureCell(tableView.cellForRow(at: indexPath!)!, avecScore: anObject as? Score)
            case .move:
                configureCell(tableView.cellForRow(at: indexPath!)!, avecScore: anObject as? Score)
                tableView.moveRow(at: indexPath!, to: newIndexPath!)
            default:
                return
        }
    }
    
    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.endUpdates()
    }
    
    /*
     // Implementing the above methods to update the table view in response to individual changes may have performance implications if a large number of changes are made simultaneously. If this proves to be an issue, you can instead just implement controllerDidChangeContent: which notifies the delegate that all section and object changes have been processed.
     
     func controllerDidChangeContent(controller: NSFetchedResultsController) {
     // In the simplest, most efficient, case, reload the table view.
     tableView.reloadData()
     }
     */
    
}

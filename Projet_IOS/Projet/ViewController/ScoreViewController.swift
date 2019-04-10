//
//  ScoreViewController.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/5/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import UIKit
import SpriteKit
import CoreData
import GameplayKit

class ScoreViewController :UIViewController, UITextFieldDelegate {
    
    var finalScore:Int? = nil
    @IBOutlet weak var nomJoueur: UITextField!
    @IBOutlet weak var scoreLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        scoreLabel.text = "Score Final : \(finalScore!)"
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func save(_ sender: Any) {
        performSegue(withIdentifier: "AddScore", sender: self)
    }
    
    @IBAction func Cancel(_ sender: Any) {
        performSegue(withIdentifier: "Cancel", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        //Ajout d'un score
        if segue.identifier == "AddScore"{
            let controller =
                ((segue.destination as! UISplitViewController).viewControllers[1] as! UINavigationController).topViewController as! TableViewController
            
            let context = controller.fetchedResultsController.managedObjectContext
            
            let nouvScore = Score(context :context )
                nouvScore.score = Int32(finalScore!)
                nouvScore.nomJoueur = nomJoueur.text!
                nouvScore.date = Date()
                    
                    // Save the context.
                    do {
                        try context.save()
                    } catch {
                        // Replace this implementation with code to handle the error appropriately.
                        // fatalError() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                        let nserror = error as NSError
                        fatalError("Unresolved error \(nserror), \(nserror.userInfo)")
                    }

        }
        
        //Annulation de l'ajout d'un score
        if segue.identifier == "Cancel"{
            let controller =
                ((segue.destination as! UISplitViewController).viewControllers[0] as! UINavigationController).topViewController as! UIViewController
        }
    }
        
    
    
    
    /*********************************
     * Protocole UITextFieldDelegate *
     *********************************/
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        
        return true
    }
    
    
}

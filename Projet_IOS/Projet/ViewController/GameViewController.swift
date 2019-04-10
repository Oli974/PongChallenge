//
//  GameViewController.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/4/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import UIKit
import SpriteKit
import GameplayKit

class GameViewController: UIViewController,GameViewControllerDelegate {
    
    func returnToMainMenu() {
    }
    
    
    var score = 0
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //viewDidLoad() crée une instance de GameScene de la même taille que la vue elle-même
        let scene = GameScene(size:view.bounds.size)
        
        //Vue qui contient une scène SpriteKit
        if let skView = self.view as! SKView?{
            
            scene.scaleMode = .aspectFill
            scene.gameViewControllerDelegate=self
            skView.presentScene(scene)
        
            skView.showsFPS = true
            skView.showsNodeCount = true
            skView.ignoresSiblingOrder = true
        }
    }
    
    //Fonction qui permet d'ajouter un score
    //Affiche la vue d'ajout de score
    func addScore(val:Int){
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        guard let storyboard = appDelegate.window?.rootViewController?.storyboard
            
        else {
            return
        }
        
        //Instantion du controller de vue ScoreViewController 
        let vc = storyboard.instantiateViewController(withIdentifier: "AddScoreView") as? ScoreViewController
        vc?.finalScore = val
        self.present(vc!, animated : true, completion : nil)
    }
    
    override func prepare(for segue : UIStoryboardSegue, sender : Any?){
        if segue.identifier == "ShowScore"{
            let controller = (segue.destination as! UINavigationController).topViewController as! TableViewController
            controller.navigationItem.leftBarButtonItem = splitViewController?.displayModeButtonItem
            controller.navigationItem.leftItemsSupplementBackButton = true
        }
        
    }
    
    override var shouldAutorotate: Bool {
        return true
    }

    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        if UIDevice.current.userInterfaceIdiom == .phone {
            return .allButUpsideDown
        } else {
            return .all
        }
    }

    override var prefersStatusBarHidden: Bool {
        return true
    }
}

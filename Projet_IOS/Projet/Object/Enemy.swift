//
//  Enemy.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/4/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import Foundation
import SpriteKit

class Enemy:Personnage{
    
    var typeEnemy:Int
    var valueEnemy:Int
    
    var inPosition:Bool
    var inAttack:Bool
    var goBack:Bool
    
    var isHit:Bool
    var isAlive:Bool
    var indexTab:Int
    
    init(type:Int,nodeIn:SKSpriteNode){
        typeEnemy = type
        inPosition = false
        inAttack = false
        goBack = false
        
        isAlive = true
        isHit = false
        indexTab = 0
        
        if type > 0 {
            valueEnemy = typeEnemy * 50
        } else {
            valueEnemy = 50
        }
        super.init(nodeIn:nodeIn)
    }
    
    func attack(){
        let launchAttack = drand48()
        
        if launchAttack < 0.1 && inPosition{
            vectorY = 20
            inAttack = true
        }
    }
    
    
    func move(ecranSize : CGSize, posJoueur:SKNode){
        
        var posX = getNode().position.x
        var posY = getNode().position.y
        
        let largeur = ecranSize.width
        let hauteur = ecranSize.height
        
        switch typeEnemy {
        case 0:
            if(posX >= (largeur - getNode().size.width) || posX <= (0-getNode().size.width)) {
                invertVectorX()
            }
            
            posX+=CGFloat(getVectorX())
            posY-=CGFloat(getVectorY())
            
            
        case 1:
            if posY > hauteur/2{
                posY-=CGFloat(getVectorY())
            }
            else if posY<=hauteur/2 && !inAttack && !goBack{
                inPosition=true
                if(posX >= (largeur - getNode().size.width) || posX <= (0-getNode().size.width)) {
                   invertVectorX()
                }
                posX+=CGFloat(getVectorX())
            }
            else if posY<=largeur/2 && goBack{
                posY+=CGFloat(getVectorY())
                
                print("GoBack :"+String(goBack))
                
                if posY >= hauteur/2 {
                    goBack = false
                }
            }
            else if inAttack{
                posY-=CGFloat(getVectorY())
                inPosition=false
                
                if posY <= 0.05*hauteur{
                    goBack=true
                    inAttack=false
                }
            }
        case 2:
            if getNode().position.x < posJoueur.position.x{
                posX+=CGFloat(getVectorX())
            }else{
                posX-=CGFloat(getVectorX())
            }
            posY-=CGFloat(getVectorY())
        default:
            return
        }
        
        if getNode().position.y < -(0.2*hauteur){
            isAlive=false
            getNode().removeFromParent()
        }
        
        getNode().position.x = posX
        getNode().position.y = posY
    }
}

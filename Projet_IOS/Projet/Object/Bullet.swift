//
//  Bullet.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/4/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import Foundation
import GameplayKit
import SpriteKit

class Bullet{
    
    var node:SKSpriteNode
    var vectorY:Int
    var isActivate:Bool
    var hasHit:Bool
    
    init(nodeIn:SKSpriteNode){
        node = nodeIn
        isActivate=false
        hasHit=false
        vectorY = 50
    }
    
    //Gestion de l'animation
    func nodeInit(w:CGFloat,h:CGFloat,pos:CGPoint){
        node.setScale(0.05)
        node.zPosition = 1
        node.position = pos
        
        node.physicsBody = SKPhysicsBody(rectangleOf: node.size)
        node.physicsBody!.affectedByGravity = false
    }
    func physicsInit(cat : UInt32, coll : UInt32, test:UInt32){
        node.physicsBody!.categoryBitMask = cat
        node.physicsBody!.collisionBitMask = coll
        node.physicsBody!.contactTestBitMask = test
    }
    func getNode() -> SKSpriteNode{ return node}
    func setNode(nodeIn:SKSpriteNode){
        node = nodeIn
    }
    func run(action : SKAction){
        node.run(action)
    }
    
    func getVectorY() -> Int {return vectorY}
    
    func move(size:CGSize){
        let hauteur = size.height
        getNode().position.y+=CGFloat(getVectorY())
        if(getNode().position.y > hauteur + getNode().size.height) {
            isActivate=false
            hasHit=true
            getNode().removeFromParent()
        }
    }
}

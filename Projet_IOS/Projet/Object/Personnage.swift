//
//  Personnage.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/4/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import SpriteKit
import Foundation

class Personnage{
    
    private var pv:Int
    private var attack:Int
    private var level:Int
    private var isAlive:Bool
    
    var vectorX:Int
    var vectorY:Int
    
    var node:SKSpriteNode
    
    init(nodeIn: SKSpriteNode){
        pv = 100
        attack = 30
        level = 1
        
        vectorX = 15
        vectorY = 10
        
        isAlive = true
        node = nodeIn
    }
    
    func setPv(nb:Int){ pv = nb }
    func getPv() -> Int { return pv}
    
    func getAttack() -> Int { return attack}
    
    func getVectorX() -> Int { return vectorX}
    func getVectorY() -> Int { return vectorY}
    
    func invertVectorX() {
        vectorX = -vectorX
    }
    
    
    //Gestion de l'animation
    func nodeInit(w:CGFloat,h:CGFloat,coord:CGPoint){
            node.setScale(0.1)
            node.position = coord
            node.zPosition = 2
            
            node.physicsBody = SKPhysicsBody(rectangleOf: node.size)
            node.physicsBody!.affectedByGravity = false
        }
        func physicsInit(cat : UInt32, coll : UInt32, test:UInt32){
            node.physicsBody!.categoryBitMask = cat
            node.physicsBody!.collisionBitMask = coll
            node.physicsBody!.contactTestBitMask = test
        }
        func setNodePhysics(physics:SKPhysicsBody?){
            node.physicsBody = physics
        }
        func getNode() -> SKSpriteNode{ return node}
        func setNode(nodeIn:SKSpriteNode){
            node = nodeIn
        }
        func run(action : SKAction){
            node.run(action)
        }
    
    
}

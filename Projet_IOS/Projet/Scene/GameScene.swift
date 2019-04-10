//
//  GameScene.swift
//  Projet
//
//  Created by Lebeau Olivier on 4/4/19.
//  Copyright © 2019 Lebeau Olivier. All rights reserved.
//

import SpriteKit
import GameplayKit

class GameScene: SKScene, SKPhysicsContactDelegate{
   
    let player:Player
    let background = SKSpriteNode(imageNamed: "fond")
    
    var gameArea:CGRect
    var enemyArray:Array<Enemy>
    var bulletArray:Array<Bullet>
    
    var score:Int
    
    var isRunningLevel:Bool
    var gameState:Bool
    var gameViewControllerDelegate:GameViewControllerDelegate?
    
    struct physicsCategories {
        static let None : UInt32 = 0       //0
        static let Player : UInt32 = 0b1   //1
        static let Bullet : UInt32 = 0b10  //2
        static let Enemy : UInt32 = 0b100  //4
        static let Bords : UInt32 = 0b1000 //6
    }
    
    func random() -> CGFloat{
        return CGFloat(Float(arc4random())/0xFFFFFFFF)
    }
    
    func random(min:CGFloat, max:CGFloat) -> CGFloat{
        return random() * (max - min)+min
    }
    
    
    override init(size: CGSize) {
        player = Player(nodeIn: SKSpriteNode(imageNamed: "player"))
        
        let maxAspectRatio:CGFloat = 16.0/9.0
        
        let playableWidth = size.height/maxAspectRatio
        let margin = (size.width - playableWidth)/2
        
        gameArea = CGRect(x:margin, y:0, width: playableWidth,height: size.height)
        score = 0
        
        enemyArray = Array()
        
        bulletArray = Array()
        
        isRunningLevel = false
        gameState = true
        
        super.init(size: size)
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    override func didMove(to view: SKView) {
        
        self.physicsWorld.contactDelegate = self
        
        //background
        background.size = self.size
        background.position = CGPoint(x: self.size.width/2, y: self.size.height/2)
        background.zPosition = 0
        self.addChild(background)
        
        //Player
        let initPos = CGPoint(x: self.size.width/2, y: self.size.height*0.1)
        
        player.nodeInit(w: self.size.width, h:self.size.height,coord: initPos)
        player.physicsInit(cat: physicsCategories.Player,
                           coll: physicsCategories.None,
                           test: physicsCategories.Enemy)
        
       
        //Bord de l'écran
        let border = SKPhysicsBody(edgeLoopFrom: self.frame)
        
        border.friction = 0
        border.restitution = 1
        
        self.physicsBody = border
        self.physicsBody!.categoryBitMask = physicsCategories.Bords
        
        self.addChild(player.getNode())
    }
    
    //Fonction qui permet de faire apparaitre les ennemis
    func spawnEnnemis(){
        if enemyArray.isEmpty{
            for i in 0..<5{
                enemyArray.append(generateEnemy(i: i))
                enemyArray[i].indexTab = i
            }
        }
        
        for i in enemyArray{
            self.addChild(i.getNode())
        }
    }
    
    //Fonction qui permet de générer les ennemis
    //Défini la position en x en fonction de sa place dans la vague qui arrive
    
    func generateEnemy(i : Int) -> Enemy{
        let espace = gameArea.maxX/5
        
        let startPoint = CGPoint(x: 5+CGFloat(i)*espace,y: self.size.height * 1.1)
        
        let typeEnemy:Int = setType()
        let enemy = Enemy(type : typeEnemy,nodeIn : SKSpriteNode(imageNamed: "enemy"))
        
        enemy.nodeInit(w: self.size.width,h: self.size.height,coord: startPoint)
        
        let physics = enemy.getNode().physicsBody
        physics!.categoryBitMask = physicsCategories.Enemy
        physics!.collisionBitMask = physicsCategories.None
        physics!.contactTestBitMask = physicsCategories.Player | physicsCategories.Bullet | physicsCategories.Bords
        enemy.setNodePhysics(physics:physics!)
        return enemy
    }
    
    
    //Fonction qui génére les projectiles
    func generateBullet(){
        if bulletArray.isEmpty{
            for i in 0...20{
                let bullet = Bullet(nodeIn: SKSpriteNode(imageNamed: "bullet"))
                
                bullet.nodeInit(w: self.size.width, h: self.size.height, pos: player.getNode().position)
                bullet.physicsInit(cat: physicsCategories.Bullet, coll: physicsCategories.None, test: physicsCategories.Enemy)
                
                bulletArray.insert(bullet,at : i)
            }
        }
    }
    
    //Fonction qui défini le type de l'ennemi en fonction du score actuel
    func setType() -> Int{
        var typeEnemy = 0
        switch score {
            case 0..<1000:
                typeEnemy = 0
            case 1000..<2000:
                typeEnemy = Int(random(min: 0, max: 1))
            default:
                typeEnemy = Int(random(min: 0, max: 3))
        }
        return typeEnemy
    }
    
    //Fonction appelée au premier clic de l'utilisateur sur la surface du jeu
    //Démarre le niveau et la génération de vagues d'ennemis
    //Les ennemis arrivant dans un premier temps en vague, puis sont générés quand un ennemi meurt
    func startNewLevel(){
        if enemyArray.isEmpty{
            self.run(SKAction.run(spawnEnnemis))
        }
        
        //génére les ennemis
        let spawnE = SKAction.run {
            
            
            let espace = self.gameArea.maxX/5
    
            for i in self.enemyArray{
                if !i.isAlive{
                    i.setPv(nb: 100+i.typeEnemy*50)
                    
                    let startPoint = CGPoint(x: 5+CGFloat(i.indexTab)*espace,y: self.size.height * 1.1)
                    
                    i.getNode().position = startPoint
                    i.typeEnemy = self.setType()
                    i.isAlive = true
                    
                    self.addChild(i.getNode())
                }
            }
           
        }
        
        //Génére les projectiles
        let generate = SKAction.run{
            var i = 0
            while i<self.bulletArray.count && (self.bulletArray[i].isActivate || self.bulletArray[i].hasHit) {
                i+=1
            }
            if(i < self.bulletArray.count){
                self.bulletArray[i].isActivate = true
                self.bulletArray[i].getNode().position.x = self.player.getNode().position.x
                self.bulletArray[i].getNode().position.y = self.player.getNode().position.y
                self.addChild(self.bulletArray[i].getNode())
            }
        }
        
        let waitToGenerate = SKAction.wait(forDuration: 0.2)
        let waitToSpawn = SKAction.wait(forDuration : 0.05)
        let generateSequence = SKAction.sequence([waitToGenerate,generate])
        let generateForever = SKAction.repeatForever(generateSequence)
        
        let spawSequence = SKAction.sequence([waitToSpawn,spawnE])
        let spawnForever = SKAction.repeatForever(spawSequence)
        
        self.run(spawnForever)
        self.run(generateForever)
    }
    
    //Permet de détecter les collisions entre les ennemis et le joueur ou entre les ennmis et les projectiles
    func didBegin(_ contact: SKPhysicsContact) {
        var body1 = SKPhysicsBody()
        var body2 = SKPhysicsBody()
        
        body1 = (contact.bodyA.categoryBitMask < contact.bodyB.categoryBitMask ? contact.bodyA : contact.bodyB )
        body2 = (contact.bodyA.categoryBitMask < contact.bodyB.categoryBitMask ? contact.bodyB : contact.bodyA )
        
        //Si il y'a collision entre un ennemi et le joueur
        if body1.categoryBitMask == physicsCategories.Player && body2.categoryBitMask == physicsCategories.Enemy{
             body1.node?.removeFromParent()
             body2.node?.removeFromParent()
            
             isRunningLevel = false
             gameState = false
        }
        
        //Si il y'a collision entre une balle et un ennemi
        if body1.categoryBitMask == physicsCategories.Bullet && body2.categoryBitMask == physicsCategories.Enemy{
            if((body1.node?.position.y as! CGFloat) < self.size.height){
                for i in 0..<enemyArray.count{
                    if body2.node == enemyArray[i].getNode(){
                        enemyArray[i].setPv(nb: enemyArray[i].getPv() - player.getAttack())
                        if(enemyArray[i].getPv() <= 0){
                            body2.node?.removeFromParent()
                            score+=enemyArray[i].valueEnemy
                            enemyArray[i].isAlive = false
                        }
                    }
                }
                for i in bulletArray{
                    if body1 == i.getNode(){
                        i.hasHit = true
                    }
                }
                
                body1.node?.removeFromParent()
            }
        }
    }
 
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if(!isRunningLevel && gameState){
            generateBullet()
            isRunningLevel = true
            startNewLevel()
        }
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        for touch : AnyObject in touches{
            
            let pointOfTouch = touch.location(in:self)
            let previousPointOfTouch = touch.previousLocation(in: self)
            
            let amountDragged = pointOfTouch.x - previousPointOfTouch.x
            
            player.getNode().position.x += amountDragged
            
            if(player.getNode().position.x > gameArea.maxX){
                player.getNode().position.x = gameArea.maxX
            }
            else if(player.getNode().position.x < 0 + player.getNode().size.width/2) {
                player.getNode().position.x = 0
            }
        }
    }
    
    override func update(_ currentTime : TimeInterval){
        if(gameState){
            if !enemyArray.isEmpty {
                for i in enemyArray{
                    if(i.isAlive){
                        i.move(ecranSize: self.size, posJoueur: player.getNode())
                        i.attack()
                    }
                }
            }
            
            
            if !bulletArray.isEmpty{
                for i in bulletArray{
                    if(i.isActivate){
                        i.move(size: self.size)
                    }
                    if i.hasHit{
                        i.isActivate=false
                        i.hasHit=false
                    }
                }
            }
        }else{
            endScene()
        }
    }
    
    //Fonction déclenchée à la fin du jeu quand le joueur perd
    //Redirige vers la vue d'ajout de scores 
    func endScene(){
        self.view?.presentScene(nil)
        gameViewControllerDelegate?.addScore(val: score)
    }
    
    
    deinit {
        print("\n THE SCENE \((type(of:self))) WAS REMOVE FROME MEMORY (DEINIT) \n ")
    }
}

protocol GameViewControllerDelegate:class {
    func addScore(val:Int)
    func returnToMainMenu()
}

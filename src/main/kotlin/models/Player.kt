package models

import kotlinx.serialization.Serializable

@Serializable
class Player(val ID: String, var password:String, var victoryPoints: Int) {

    fun addVictoryPoints(points: Int){
        victoryPoints += points
    }

    fun hasThisPassword(password: String):Boolean{
        return this.password == password
    }
}



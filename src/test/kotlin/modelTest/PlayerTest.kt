package modelTest

import models.Player
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PlayerTest {
    val playerID = "player"
    val password = "password"
    @Before
    fun setup(){

    }

    @Test
    fun addVictoryPoints_WhenAddZero_VictoryPointsDontChange(){
        val expectedValue = 1
        var player = Player(playerID,password,expectedValue)
        val pointsToAdd = 0

        player.addVictoryPoints(pointsToAdd)

        Assert.assertEquals(expectedValue,player.victoryPoints)
    }

    @Test
    fun addVictoryPoints_WhenAddOne_VictoryPointsIncrement(){
        val victoryPointsAtStart = 1
        val pointsToAdd = 1
        val expectedValue = victoryPointsAtStart+pointsToAdd
        var player = Player(playerID,password,victoryPointsAtStart)


        player.addVictoryPoints(pointsToAdd)

        Assert.assertEquals(expectedValue,player.victoryPoints)
    }

    @Test
    fun hasThisPassword_WhenPasswordIsIncorrect_ReturnFalse(){
        val passwordToCheck = password + "Other"
        var player = Player(playerID,password,0)

        val result = player.hasThisPassword(passwordToCheck)

        Assert.assertFalse(result)
    }

    @Test
    fun hasThisPassword_WhenPasswordIsCorrect_ReturnTrue(){
        val passwordToCheck = password
        var player = Player(playerID,password,0)

        val result = player.hasThisPassword(passwordToCheck)

        Assert.assertTrue(result)
    }


}
package useCasesTest

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import models.Player
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever
import repositories.IPlayerRepository
import useCases.FindNewRival

@RunWith(MockitoJUnitRunner::class)
class FindNewRivalTest {


    private lateinit var players: List<Player>
    private lateinit var playerRepository: IPlayerRepository
    private lateinit var findNewRivalForPlayer: FindNewRival

    @Before
    fun setUp() {
        players = listOf(
            Player("Player1", "Password1", 5),
            Player("Player2", "Password2", 4),
            Player("Player3", "Password3", 9),
            Player("Player4", "Password4", 15)
        )
        playerRepository = mock(IPlayerRepository::class.java)
        findNewRivalForPlayer = FindNewRival(playerRepository)
    }


    @Test
    fun Execute_GetPlayerFromRepository()=  runTest{
        val player = players.first()

        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        playerRepository.stub { onBlocking { GetPlayers() }.thenReturn(players) }

        var rival = async{ findNewRivalForPlayer.execute(player.ID)}
        yield()
        Mockito.verify(playerRepository).Get(player.ID)

    }

    @Test
     fun Execute_GetAllPlayersFromRepository()= runTest{
        val player = players.first()
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        playerRepository.stub { onBlocking { GetPlayers() }.thenReturn(players) }

        async{findNewRivalForPlayer.execute(player.ID)}
        yield()
        Mockito.verify(playerRepository).GetPlayers()
    }

    @Test
    fun Execute_ReturnsAnotherPlayerID()= runTest {
        val player = players.first()
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        playerRepository.stub { onBlocking { GetPlayers() }.thenReturn(players) }
        val possiblePlayersIDs = players.filter { p -> p != player }.map { p -> p.ID }

        val result = findNewRivalForPlayer.execute(player.ID)

        Assert.assertTrue(possiblePlayersIDs.any{ pp -> pp == result })
        Assert.assertTrue(result != player.ID)
    }

    @Test
    fun Execute_ThereIsRivalWithFiveVictoryPointOrLessOfDifference_ReturnsThisRival()= runTest {
        val player = players.first()
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        playerRepository.stub { onBlocking { GetPlayers() }.thenReturn(players) }
        val expectedRival = players[2]

        var result = findNewRivalForPlayer.execute(player.ID)

        Assert.assertEquals(expectedRival.ID, result)
    }

    @Test
    fun Execute_ThereIsNotRivalWithFiveVictoryPointOrLessOfDifferenceAndThereIsNewPLayers_ReturnsRandomNewPlayer()= runTest {
        val player = players[3];
        var expectedRivalsIDs = players.filter { p -> p.victoryPoints < 5 }.map { p -> p.ID }
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        playerRepository.stub { onBlocking { GetPlayers() }.thenReturn(players) }

        var result = findNewRivalForPlayer.execute(player.ID)

        Assert.assertTrue(expectedRivalsIDs.contains(result))
    }

}
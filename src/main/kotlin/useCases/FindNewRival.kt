package useCases


import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import models.Player
import repositories.IPlayerRepository
import kotlin.random.Random
class FindNewRival(val playerRepository: IPlayerRepository) {
    suspend fun execute(playerID:String?):String  {
        if(playerID==null) throw Exception("id sin valor")
        val player = playerRepository.Get(playerID) ?: throw Exception("Jugador no encontrado")
        val anotherPlayers = getPlayersWithOut(playerID)
        return obtainRivalForThisPlayerFromThisAnotherPlayers(player, anotherPlayers).ID
    }

    private fun obtainRivalForThisPlayerFromThisAnotherPlayers(player: Player, anotherPlayers: List<Player>): Player {
        var possibleRivals = getWorthyRivalsForPlayer(player, anotherPlayers)
        if (possibleRivals.isNotEmpty())return getRandomPlayer(possibleRivals);

        possibleRivals = getNewPlayers(anotherPlayers);
        if (possibleRivals.isNotEmpty())return getRandomPlayer(possibleRivals);

        return getRandomPlayer(anotherPlayers);
    }

    private fun getNewPlayers(players: List<Player>): List<Player> {
        return players.filter{player -> player.victoryPoints<5}
    }

    private fun getRandomPlayer(players: List<Player>): Player {
        require(players.isNotEmpty()) { "La lista de jugadores está vacía" }
        return players[Random.nextInt(players.size)];
    }

    private fun getWorthyRivalsForPlayer(player: Player, anotherPlayers: List<Player>): List<Player> {
        return anotherPlayers.filter{anotherPlayer -> Math.abs(anotherPlayer.victoryPoints - player.victoryPoints)<= 5}
    }

    private suspend fun getPlayersWithOut(playerID: String): List<Player> {
        return playerRepository.GetPlayers().filter { p-> p.ID != playerID }
    }
}


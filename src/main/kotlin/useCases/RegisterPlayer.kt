package useCases

import DTOs.LoginData
import DTOs.LoginResultDTO
import models.Player
import repositories.IPlayerRepository

class RegisterPlayer(val playerRepository: IPlayerRepository) {
    suspend fun execute(player: LoginData?): LoginResultDTO {

        if(player == null) {
            throw NullPointerException("Not player info sent")
        }

        if(playerRepository.Get(player.id) != null) {
            return LoginResultDTO("Player already exists",false)
        }

        if(player.password.length <5){
            return LoginResultDTO("Password must have at least 5 characters",false)
        }

        playerRepository.Add(Player(player.id, player.password, 0))
        return LoginResultDTO("",true)
    }
}
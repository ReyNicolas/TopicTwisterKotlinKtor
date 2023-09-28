package useCases

import DTOs.LoginResultDTO
import repositories.IPlayerRepository

class LoginPlayer(val playerRepository: IPlayerRepository) {
    suspend fun execute(id:String?,password:String?): LoginResultDTO {
        if(id==null) throw NullPointerException("id sin valor")
        if(password==null) throw NullPointerException("password sin valor")

        val player = playerRepository.Get(id)
        if(player==null) return LoginResultDTO("jugador con id: $id no encontrado",false)
        if(player.password!=password) return LoginResultDTO("password incorrecta",false)
        return LoginResultDTO("",true);
    }
}

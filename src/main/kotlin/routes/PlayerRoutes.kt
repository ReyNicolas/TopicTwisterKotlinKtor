package routes

import DTOs.LoginData
import DTOs.PlayerData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import repositories.IPlayerRepository
import repositories.PlayerRepository
import useCases.FindNewRival
import useCases.LoginPlayer
import DTOs.LoginResultDTO
import useCases.RegisterPlayer

fun Route.playerRouting() {

    var playerRepository: IPlayerRepository = PlayerRepository();
    var registerPlayer: RegisterPlayer = RegisterPlayer(playerRepository)
    var loginPlayer: LoginPlayer = LoginPlayer(playerRepository)
    var findNewRival: FindNewRival = FindNewRival(playerRepository)

    route("/player") {
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val player = async {
                 playerRepository.Get(id)}.await()?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(player)
        }
       // put("{id}") {
        put() {
            val playerData: PlayerData? = call.receive<PlayerData>()
            playerData?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val player = playerRepository.Get(playerData.id)?: return@put call.respondText(
                "No customer with id ${playerData.id}",
                status = HttpStatusCode.NotFound
            )
            player.password = playerData.password
            player.victoryPoints = playerData.victoryPoints

            playerRepository.Update(player);

            call.respondText("Player updated correctly", status = HttpStatusCode.Accepted)
        }
        get("login/{id}/{password}") {
            val id:String? = call.parameters["id"]
            val password:String? = call.parameters["password"]
            var result: LoginResultDTO

            try{
                result= loginPlayer.execute(id,password)
            }
            catch (e: Exception){
                return@get call.respondText(
                    e.message.toString(),
                    status = HttpStatusCode.BadRequest
                )
            }
            call.respond(result)
        }
        post {
            val player = call.receive<LoginData>()
            var result: LoginResultDTO
            try{
                result =  registerPlayer.execute(player);
            }
            catch (e:Exception){
                return@post call.respondText(
                    e.message.toString(),
                    status = HttpStatusCode.BadRequest
                )
            }
            call.respond(result)
        //call.respondText("Player created correctly", status = HttpStatusCode.Accepted)
        }
        get("getRivalForPlayer/{id}") {
            val id:String? = call.parameters["id"]
            var result:String

            try{
                result= findNewRival.execute(id)
            }
            catch (e: Exception){
                return@get call.respondText(
                    e.message.toString(),
                    status = HttpStatusCode.BadRequest
                )
            }
            call.respond(result)
        }
    }
}

fun Application.registerPlayerRoutes(){
    routing {
        playerRouting()
    }
}
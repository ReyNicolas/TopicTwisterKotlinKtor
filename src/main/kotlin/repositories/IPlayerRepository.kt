package repositories

import DTOs.PlayerData
import com.google.api.core.ApiFuture
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreBundle
import com.google.common.util.concurrent.MoreExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
//import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import models.Player
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import kotlin.reflect.KClass


interface IPlayerRepository {
    suspend fun GetPlayers():List<Player>
    suspend fun Get(id:String): Player?
    suspend fun Update(player: Player):Boolean
    suspend fun Add(player: Player):Boolean
}

class PlayerRepository: IPlayerRepository{

    init {
        //val serviceAccount = FileInputStream("ktor-player-api-id-firebase-adminsdk-sfhqm-c422b90dab.json")
        val serviceAccount =this.javaClass.classLoader.getResourceAsStream("ktor-player-api-id-firebase-adminsdk-sfhqm-c422b90dab.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()
        //.setCredentials(GoogleCredentials.fromStream(serviceAccount))

        FirebaseApp.initializeApp(options)
    }
    val db = FirestoreClient.getFirestore()
    override suspend fun GetPlayers(): List<Player> {
        val snapshots = db.collection("players").get().get().documents
        return snapshots.mapNotNull { ConvertDataToPlayer(it.toObject(PlayerData::class.java)) }
    }

    override suspend fun  Get(id: String): Player? {
        return withContext(Dispatchers.IO) {
            val snapshot = db.collection("players").document(id).get().get()
            if (snapshot.exists()) {
               ConvertDataToPlayer(snapshot.toObject(PlayerData::class.java))
            } else {
                null
            }
        }
    }

    override suspend fun Update(player: Player): Boolean {
        var playerToUpdate: Player? =  Get(player.ID)
        if(playerToUpdate == null) throw Exception("player with ${player.ID} doesnt exist")

        playerToUpdate.password = player.password
        playerToUpdate.victoryPoints = player.victoryPoints
        db.collection("players").document(playerToUpdate.ID).set(ConvertPlayerToData(playerToUpdate))
        return true
    }

    override suspend fun Add(player: Player): Boolean {
        if(Get(player.ID) != null) throw Exception("ID already exists")
        db.collection("players").document(player.ID).set(ConvertPlayerToData(player))
        return true
    }

    private fun ConvertDataToPlayer(data: PlayerData?):Player{
        if(data == null) return throw Exception("data error")
        return Player(data.id,data.password,data.victoryPoints)
    }

    private fun ConvertPlayerToData(player: Player?):PlayerData{
        if(player == null) return throw Exception("data error")
        return PlayerData(player.ID,player.password,player.victoryPoints)
    }

}
fun<T: Any> T.getClass(): KClass<T> {
    return javaClass.kotlin
}


package utils

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.FileInputStream

object FirebaseUtils {
    init {
        val serviceAccount = FileInputStream("ktor-player-api-id-firebase-adminsdk-sfhqm-c422b90dab.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
    }
    val db = FirestoreClient.getFirestore()
}
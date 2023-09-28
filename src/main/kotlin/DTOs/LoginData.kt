package DTOs

import kotlinx.serialization.Serializable

@Serializable
data class LoginData(val id: String, var password: String)


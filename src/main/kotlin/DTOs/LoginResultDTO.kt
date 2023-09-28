package DTOs

import kotlinx.serialization.Serializable

@Serializable
data class LoginResultDTO(val ErrorMessage:String,val SuccessAuthentication:Boolean )
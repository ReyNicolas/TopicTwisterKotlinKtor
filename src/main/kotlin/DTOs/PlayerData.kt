package DTOs

import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(val id: String, val password:String, var victoryPoints: Int)
{
    constructor():this("", "", 0)
}


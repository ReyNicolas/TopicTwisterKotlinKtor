package useCasesTest

import DTOs.LoginData
import kotlinx.coroutines.test.runTest
import models.Player
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.stub
import repositories.IPlayerRepository
import useCases.LoginPlayer

class LoginPlayerTest {
        val password = "12345"
    val playerID = "player"
    val player= Player(playerID,password,0)
    private lateinit var playerRepository: IPlayerRepository
    private lateinit var loginPlayer: LoginPlayer

    @Before
    fun setUp() {
        playerRepository = Mockito.mock(IPlayerRepository::class.java)
        loginPlayer = LoginPlayer(playerRepository)
    }

    @Test
    fun execute_PlayerIDDoesntExist_returnMessageAndSuccessAuthenticationFalse() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(null) }
        val expectedMessage = "jugador con id: ${playerID} no encontrado";

        val result = loginPlayer.execute(playerID,password)

        Assert.assertEquals(expectedMessage,result.ErrorMessage)
        Assert.assertFalse(result.SuccessAuthentication)
    }
    @Test
    fun execute_PlayerExistsButIncorrectPassword_returnMessageAndSuccessAuthenticationFalse() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        val passwordToCheck = player.password +"wrong"
        val expectedMessage = "password incorrecta";

        val result =  loginPlayer.execute(playerID,passwordToCheck)

        Assert.assertEquals(expectedMessage,result.ErrorMessage)
        Assert.assertFalse(result.SuccessAuthentication)
    }

    @Test
    fun execute_PlayerExistsAndPasswordIsCorrect_returnSuccessAuthenticationTrue() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }

        val result = loginPlayer.execute(playerID,password)

        Assert.assertEquals("",result.ErrorMessage)
        Assert.assertTrue(result.SuccessAuthentication)
    }

}
package useCasesTest

import DTOs.LoginData
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import models.Player
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.anyVararg
import org.mockito.kotlin.stub
import repositories.IPlayerRepository
import useCases.FindNewRival
import useCases.RegisterPlayer
import java.lang.NullPointerException
import kotlin.reflect.typeOf
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RegisterPlayerTest {

    val shortPassword = "1234"
    val password = "12345"
    val playerID = "player"
    val player= Player(playerID,password,0)
    val correctLoginData = LoginData(playerID,password)
    val incorrectLoginData = LoginData(playerID,shortPassword)
    private lateinit var playerRepository: IPlayerRepository
    private lateinit var registerPlayer: RegisterPlayer

    @Before
    fun setUp() {
        playerRepository = Mockito.mock(IPlayerRepository::class.java)
        registerPlayer = RegisterPlayer(playerRepository)
    }
     //@Test
    fun Execute_LoginDataIsNull_ThrowException() = runTest{
       val exception = assertFailsWith<NullPointerException>{async { registerPlayer.execute(null) }.await() }
    //        exceptionClass = NullPointerException::class,
     //       message = "Not player info sent",
       //     block = { async { registerPlayer.execute(null) }.await() }
        //)
         assertEquals("Not player info sent", exception.message)
    }
    @Test
    fun execute_PlayerIDAlreadyExist_returnMessageAndSuccessAuthenticationFalse() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(player) }
        val loginData =LoginData(playerID, password)
        val expectedMessage = "Player already exists";

        val result = registerPlayer.execute(loginData)

        Assert.assertEquals(expectedMessage,result.ErrorMessage)
        Assert.assertFalse(result.SuccessAuthentication)
    }
    @Test
        fun execute_PasswordHasLessThatFiveCharacters_returnMessageAndSuccessAuthenticationFalse() = runTest {
            playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(null) }
            val expectedMessage = "Password must have at least 5 characters";

            val result = registerPlayer.execute(incorrectLoginData)

            Assert.assertEquals(expectedMessage,result.ErrorMessage)
            Assert.assertFalse(result.SuccessAuthentication)
        }

    @Test
    fun execute_PlayerDoenstExistAndPasswordHasFiveCharactersOrMore_returnSuccessAuthenticationTrue() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(null) }

        val result = registerPlayer.execute(correctLoginData)

        Assert.assertEquals("",result.ErrorMessage)
        Assert.assertTrue(result.SuccessAuthentication)
    }
    @Test
    fun execute_MustAddToTheRepositoryTheNewPlayer() = runTest {
        playerRepository.stub { onBlocking { Get(player.ID) }.thenReturn(null) }

        val result = registerPlayer.execute(correctLoginData)

        yield()
        Mockito.verify(playerRepository).Add(anyVararg())

    }

}
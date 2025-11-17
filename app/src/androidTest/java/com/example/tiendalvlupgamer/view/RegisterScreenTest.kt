package com.example.tiendalvlupgamer.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerScreen_displaysAllElements() {
        composeTestRule.setContent {
            RegisterScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithTag("nameInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("lastNameInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("usernameInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("emailInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("passwordInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("registerButton").assertIsDisplayed()
    }

    @Test
    fun registerScreen_successfulRegistration() {
        composeTestRule.setContent {
            RegisterScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithTag("nameInput").performTextInput("Test")
        composeTestRule.onNodeWithTag("lastNameInput").performTextInput("User")
        composeTestRule.onNodeWithTag("usernameInput").performTextInput("testuser")
        composeTestRule.onNodeWithTag("emailInput").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("passwordInput").performTextInput("Password123")
        composeTestRule.onNodeWithTag("registerButton").performClick()

        // As with LoginScreen, we mainly check that the process doesn't crash.
        // Verifying navigation would require a more complex test setup.
    }
}
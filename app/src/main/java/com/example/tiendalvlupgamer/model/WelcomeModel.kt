package com.example.tiendalvlupgamer.model

enum class WelcomeOption {
    REGISTER, LOGIN, GUEST
}

data class WelcomeState(
    val selected: WelcomeOption? = null
)

sealed class WelcomeNavigation {
    object ToRegister : WelcomeNavigation()
    object ToLogin : WelcomeNavigation()
    object ToGuest : WelcomeNavigation()
}
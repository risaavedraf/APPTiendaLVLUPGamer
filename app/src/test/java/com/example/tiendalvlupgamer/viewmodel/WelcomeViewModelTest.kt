package com.example.tiendalvlupgamer.viewmodel

import app.cash.turbine.test
import com.example.tiendalvlupgamer.model.WelcomeNavigation
import com.example.tiendalvlupgamer.model.WelcomeOption
import com.example.tiendalvlupgamer.ui.WelcomeViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class WelcomeViewModelTest : StringSpec({

     lateinit var viewModel: WelcomeViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = WelcomeViewModel()
    }

    "onRegister should update uiState and emit navigation" { 
        viewModel.onRegister()
        viewModel.uiState.value.selected shouldBe WelcomeOption.REGISTER
        viewModel.navigation.test { 
            awaitItem() shouldBe WelcomeNavigation.ToRegister
        }
    }

    "onLogin should update uiState and emit navigation" { 
        viewModel.onLogin()
        viewModel.uiState.value.selected shouldBe WelcomeOption.LOGIN
        viewModel.navigation.test { 
            awaitItem() shouldBe WelcomeNavigation.ToLogin
        }
    }

    "onGuest should update uiState and emit navigation" { 
        viewModel.onGuest()
        viewModel.uiState.value.selected shouldBe WelcomeOption.GUEST
        viewModel.navigation.test { 
            awaitItem() shouldBe WelcomeNavigation.ToGuest
        }
    }
})



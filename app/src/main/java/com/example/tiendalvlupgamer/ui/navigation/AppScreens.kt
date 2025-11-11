package com.example.tiendalvlupgamer.ui.navigation

// ui/navigation/AppScreens.kt
sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object WelcomeScreen : AppScreens("welcome_screen")
    object HomeScreen : AppScreens("home_screen")
    object SearchScreen : AppScreens("search")
    object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
    object EventDetailScreen : AppScreens("event_detail_screen/{eventId}") {
        fun createRoute(eventId: String) = "event_detail_screen/$eventId"
    }
    object CatalogScreen : AppScreens("catalog_screen")
    object CartScreen : AppScreens("cart")
    object ProfileScreen : AppScreens("profile_screen")
    object MenuScreen : AppScreens("menu_screen")
    object EventsScreen : AppScreens("events_screen")


}
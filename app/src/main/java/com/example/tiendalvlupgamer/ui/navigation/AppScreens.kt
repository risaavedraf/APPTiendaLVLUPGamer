package com.example.levelupgamer.ui.navigation

// ui/navigation/AppScreens.kt
sealed class AppScreens(val route: String) {
    object LoginScreen : AppScreens("login_screen")
    object HomeScreen : AppScreens("home_screen")

    object SearchScreen : AppScreens("search")
    object ProductDetailScreen : AppScreens("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
    object CatalogScreen : AppScreens("catalog_screen")


}
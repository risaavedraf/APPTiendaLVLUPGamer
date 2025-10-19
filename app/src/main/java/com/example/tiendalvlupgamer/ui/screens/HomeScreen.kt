import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupgamer.ui.components.ProductCard
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.R
import com.example.tiendalvlupgamer.model.local.AppDatabase

import com.example.tiendalvlupgamer.ui.theme.Orbitron
import com.example.tiendalvlupgamer.viewmodel.ProductViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // 1. Obtiene el DAO a través del contexto local.
    val productDao = AppDatabase.get(LocalContext.current).productDao()
    val reviewDao = AppDatabase.get(LocalContext.current).reviewDao()
    // 2. Crea e inyecta el ViewModel usando la Factory que ya tienes.
    val viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productDao,reviewDao ))

    // 3. Observa el mapa de productos agrupados por categoría directamente desde el ViewModel.
    val productsByCategory by viewModel.productsByCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize() // Usa fillMaxSize para que ocupe toda la pantalla
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TopAppBar se mantiene igual.
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(98.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "LEVEL-UP GAMER",
                        fontFamily = Orbitron
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        // 4. Itera directamente sobre el mapa (productsByCategory) para mostrar los datos.
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            productsByCategory.forEach { (category, products) ->
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = category,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp) // Añade espacio entre tarjetas
                        ) {
                            items(products) { product ->
                                ProductCard(
                                    modifier = Modifier.width(300.dp), // Ancho de la tarjeta
                                    product = product,
                                    onClick = {
                                        navController.navigate(
                                            AppScreens.ProductDetailScreen.createRoute(
                                                product.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
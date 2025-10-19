package com.example.tiendalvlupgamer.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.tiendalvlupgamer.R

@Database(entities = [ProductEntity::class, ReviewEntity::class, CartItemEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun reviewDao(): ReviewDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback()) // El callback sigue aquí
                    .build()
                    .also { INSTANCE = it }
            }
        
        // El callback ahora accede a la instancia de forma segura
        private class AppDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // La lógica de la corutina se mantiene, pero la obtención de la instancia es más segura
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        database.productDao().insertar(PREPOPULATE_DATA)
                    }
                }
            }
        }

        // Tu lista de productos no ha cambiado
        private val PREPOPULATE_DATA = listOf(
            ProductEntity("JM001","Juegos de Mesa","Catan",29990,"Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos.",R.drawable.catan),
            ProductEntity("JM002","Juegos de Mesa","Carcassonne",24990,"Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de aprender.",R.drawable.carcassonne),
            ProductEntity("AC001","Accesorios","Controlador Inalámbrico Xbox Series X",59990,"Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC.",R.drawable.xbox_controller),
            ProductEntity("AC002","Accesorios","Auriculares Gamer HyperX Cloud II",79990,"Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad durante largas sesiones de juego.",R.drawable.hyperx),
            ProductEntity("CO001","Consolas","PlayStation 5",549990,"La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.",R.drawable.playstation5),
            ProductEntity("CG001","Computadores Gamers","PC Gamer ASUS ROG Strix",1299990,"Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional en cualquier juego.",R.drawable.pc_asus),
            ProductEntity("SG001","Sillas Gamers","Silla Gamer Secretlab Titan",349990,"Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas.",R.drawable.silla_gamer),
            ProductEntity("MS001","Mouse","Mouse Gamer Logitech G502 HERO",49990,"Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización.",R.drawable.logitec_mouse),
            ProductEntity("MP001","Mousepad","Mousepad Razer Goliathus Extended Chroma",29990,"Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse.",R.drawable.mousepad),
            ProductEntity("PP001","Poleras Personalizadas","Polera Gamer Personalizada 'Level-Up'",14990,"Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.",R.drawable.polera)
        )
    }
}
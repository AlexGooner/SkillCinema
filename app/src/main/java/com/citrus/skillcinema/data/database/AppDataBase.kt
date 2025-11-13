package com.citrus.skillcinema.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [SavedFilm::class, SavedCollection::class, WatchedFilm::class],
    version = 6
)
abstract class AppDataBase : RoomDatabase() {


    abstract fun savedFilmDao(): SavedFilmDao
    abstract fun savedCollectionDao(): SavedCollectionDao
    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val savedCollectionDao = getDatabase(context).savedCollectionDao()
                            val initialCollections = listOf(
                                SavedCollection("Любимые"),
                                SavedCollection("Хочу посмотреть")
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                savedCollectionDao.insertAll(initialCollections)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        Log.d("DATABASE", "Applying migration from 1 to 2")
        db.execSQL(
            """
                 CREATE TABLE IF NOT EXISTS `watched_films` (
                `film_id` INTEGER PRIMARY KEY NOT NULL,
                `is_watched` INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
    }

}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {

        Log.d("DATABASE", "Applying migration from 2 to 3")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `bookmark_films` (
                `film_id` INTEGER PRIMARY KEY NOT NULL,
                `is_bookmark` INTEGER NOT NULL DEFAULT 0,
                `name` TEXT NOT NULL,
                `genres` TEXT NOT NULL,
                `rating` REAL,
                `url` TEXT NOT NULL
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `liked_films` (
                `film_id` INTEGER PRIMARY KEY NOT NULL,
                `is_liked` INTEGER NOT NULL DEFAULT 0,
                `name` TEXT NOT NULL,
                `genres` TEXT NOT NULL,
                `rating` REAL,
                `url` TEXT NOT NULL
            )
        """.trimIndent()
        )

        Log.d("DATABASE", "migration from 2 to 3 complete")
    }
}
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                    CREATE TABLE IF NOT EXISTS `bookmark_films_new` (
                `film_id` INTEGER PRIMARY KEY NOT NULL,
                `is_liked` INTEGER NOT NULL DEFAULT 0
            )
                """.trimIndent()

        )
        db.execSQL("DROP TABLE IF EXISTS `bookmark_films`")

        db.execSQL("ALTER TABLE `bookmark_films_new` RENAME TO `bookmark_films`")

        db.execSQL(
            """
                    CREATE TABLE IF NOT EXISTS `liked_films_new` (
                `film_id` INTEGER PRIMARY KEY NOT NULL,
                `is_liked` INTEGER NOT NULL DEFAULT 0
            )
                """.trimIndent()

        )
        db.execSQL("DROP TABLE IF EXISTS `liked_films`")

        db.execSQL("ALTER TABLE `liked_films_new` RENAME TO `liked_films`")
    }

}

val MIGRATION_4_5 = object  : Migration(4,5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE saved_films_new (
                id INTEGER PRIMARY KEY NOT NULL,
                collection_name TEXT NOT NULL,
                film_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                genres TEXT NOT NULL,
                rating REAL,
                url TEXT NOT NULL,
                UNIQUE(collection_name, film_id) ON CONFLICT IGNORE
            )
        """.trimIndent())


        db.execSQL("DROP TABLE saved_films")
        db.execSQL("ALTER TABLE saved_films_new RENAME TO saved_films")
    }
}

val MIGRATION_5_6 = object : Migration(5,6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE liked_films")
        db.execSQL("DROP TABLE bookmark_films")

    }

}

package info.aliavci.aavci.mynotes.util.dbflow

import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by Ali Avci
 * Version
 */
@Database(name = MainDB.NAME, version = MainDB.VERSION)
object MainDB {
    const val NAME = "MainDB"
    const val VERSION = 1
}
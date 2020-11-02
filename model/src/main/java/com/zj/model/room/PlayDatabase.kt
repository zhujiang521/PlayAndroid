package com.zj.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zj.model.room.dao.BannerBeanDao
import com.zj.model.room.dao.BrowseHistoryDao
import com.zj.model.room.dao.HotKeyDao
import com.zj.model.room.dao.ProjectClassifyDao
import com.zj.model.room.entity.Article
import com.zj.model.room.entity.BannerBean
import com.zj.model.room.entity.HotKey
import com.zj.model.room.entity.ProjectClassify


/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Database(
    entities = [ProjectClassify::class, Article::class, HotKey::class, BannerBean::class],
    version = 2
)
abstract class PlayDatabase : RoomDatabase() {

    abstract fun projectClassifyDao(): ProjectClassifyDao

    abstract fun browseHistoryDao(): BrowseHistoryDao

    abstract fun hotKeyDao(): HotKeyDao

    abstract fun bannerBeanDao(): BannerBeanDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PlayDatabase? = null

        fun getDatabase(context: Context): PlayDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayDatabase::class.java,
                    "play_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //创建新表connect_prod并添加对应的字段
        //PRIMARY KEY(id)将id设置为主键，NOT NULL设置对应的键不能为空
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `banner_bean` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`desc` TEXT NOT NULL, `id` INTEGER NOT NULL, `imagePath` TEXT NOT NULL, `isVisible` INTEGER NOT NULL, " +
                    "`order` INTEGER NOT NULL, `title` TEXT NOT NULL, `type` INTEGER NOT NULL, `url` TEXT NOT NULL, `file_path` TEXT NOT NULL)"
        )
    }
}
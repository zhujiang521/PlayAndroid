package com.zj.play.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zj.play.room.dao.BrowseHistoryDao
import com.zj.play.room.dao.ProjectClassifyDao
import com.zj.play.room.entity.Article
import com.zj.play.room.entity.ProjectClassify

/**
 * 版权：联想 版权所有
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Database(entities = [ProjectClassify::class, Article::class], version = 1)
abstract class PlayDatabase : RoomDatabase() {

    abstract fun projectClassifyDao(): ProjectClassifyDao

    abstract fun browseHistoryDao(): BrowseHistoryDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}

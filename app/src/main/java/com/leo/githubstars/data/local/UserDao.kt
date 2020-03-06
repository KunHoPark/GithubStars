package com.leo.githubstars.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import io.reactivex.Flowable

/**
 * DB 쿼리
 * @author LeoPark
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM bookmark_user_table ORDER BY LOWER(login) ASC")
    fun getLiveUserData(): LiveData<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table ORDER BY LOWER(login) ASC")
    fun getUserData(): List<UserData>

    @Query("SELECT * FROM bookmark_user_table ORDER BY LOWER(login) ASC")
    fun getUserDataRx(): Flowable<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getUserDataById(id: Int): UserData

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getUserDataRxByCoinId(id: Int): Flowable<UserData>

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getLiveUserDataByCoinId(id: Int): LiveData<UserData>

    @Query("SELECT * FROM bookmark_user_table ORDER BY LOWER(login) limit :limit offset :offset")
    fun queryUserDataRx(limit:Int, offset:Int): Flowable<List<UserData>>

    @RawQuery(observedEntities = [UserData::class])
    fun searchLiveUserDataRaw(query: SupportSQLiteQuery): LiveData<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table WHERE login LIKE :keyword ORDER BY LOWER(login) ASC")
    fun searchLiveUserDataRaw(keyword: String) : Flowable<List<UserData>>

    @Insert
    fun insert(userData: UserData)

    @Insert
    fun insertAll(userData: List<UserData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceAll(userData: List<UserData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(userData: UserData)

    @Delete
    fun delete(userData: UserData)

    @Query("DELETE FROM bookmark_user_table")
    fun deleteAll()

    @Update
    fun update(userData: UserData)

}

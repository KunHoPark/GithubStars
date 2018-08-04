package com.leo.githubstars.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM bookmark_user_table ORDER BY login ASC")
    fun getLiveUserData(): LiveData<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table ORDER BY login ASC")
    fun getUserData(): List<UserData>

    @Query("SELECT * FROM bookmark_user_table ORDER BY login ASC")
    fun getUserDataRx(): Flowable<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getUserDataById(id: Int): UserData

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getUserDataRxByCoinId(id: Int): Flowable<UserData>

    @Query("SELECT * FROM bookmark_user_table WHERE id = (:id)")
    fun getLiveUserDataByCoinId(id: Int): LiveData<UserData>

    @Query("SELECT * FROM bookmark_user_table ORDER BY login limit :limit offset :offset")
    fun queryUserDataRx(limit:Int, offset:Int): Flowable<List<UserData>>

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

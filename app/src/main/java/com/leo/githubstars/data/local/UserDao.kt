package com.leo.githubstars.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Query("SELECT * FROM bookmark_user_table")
    fun getLiveUserData(): LiveData<List<UserData>>

    @Query("SELECT * FROM bookmark_user_table")
    fun getUserData(): List<UserData>

    @Query("SELECT * FROM bookmark_user_table")
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
    fun insert(coinWalletEntity: UserData)

    @Insert
    fun insertAll(coinWalletEntities: List<UserData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceAll(gettyImages: List<UserData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replace(gettyImage: UserData)

    @Delete
    fun delete(coinWalletEntity: UserData)

    @Query("DELETE FROM bookmark_user_table")
    fun deleteAll()

    @Update
    fun update(coinWalletEntity: UserData)

}

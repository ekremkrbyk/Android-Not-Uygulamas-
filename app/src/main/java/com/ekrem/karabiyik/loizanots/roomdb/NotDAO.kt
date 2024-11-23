package com.ekrem.karabiyik.loizanots.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ekrem.karabiyik.loizanots.model.Notlar
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface NotDAO {
    @Query("Select * from not_table")
    fun getAll() : Flowable<List<Notlar>>
    @Query("Select * from not_table where id= :id")
    fun findByIdAraVeBul(id:Int) : Flowable<Notlar>
    @Insert
    fun insert(Not:Notlar) :Completable
    @Delete
    fun detele(Not:Notlar) :Completable
}
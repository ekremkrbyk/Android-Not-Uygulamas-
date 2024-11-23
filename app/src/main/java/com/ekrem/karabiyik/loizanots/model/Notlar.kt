package com.ekrem.karabiyik.loizanots.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="not_table")
data class Notlar (
    @ColumnInfo(name = "Not_baslik") //Başlık kolonu
    var baslik:String?,
    @ColumnInfo(name = "Not") //Not kolonu
    var not:String,
){
    @PrimaryKey(autoGenerate = true)//Otomatik artışa alınmıştır.
    var id=0
}
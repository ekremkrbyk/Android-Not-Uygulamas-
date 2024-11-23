package com.ekrem.karabiyik.loizanots

import com.ekrem.karabiyik.loizanots.model.Notlar

interface itemDeleteicin {
    fun sil(notlar: Notlar)
    fun duzenle(notId:Int)
}
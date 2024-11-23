package com.ekrem.karabiyik.loizanots.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ekrem.karabiyik.loizanots.R
import com.ekrem.karabiyik.loizanots.databinding.RecyleRowBinding
import com.ekrem.karabiyik.loizanots.itemDeleteicin
import com.ekrem.karabiyik.loizanots.model.Notlar
import com.ekrem.karabiyik.loizanots.view.mainScreenDirections

class NotsAdapter(val notlistesi:List<Notlar>,private val listener:itemDeleteicin):RecyclerView.Adapter<NotsAdapter.NotViewHolder>() {
    //notların listesi ve silme işlemi için listener tanımlanmıştır.
var secilenNot:Notlar?=null //Seçilen notu akılda tutmak için değişken,bu değişkenin notlar sınıfından bir obje olduğu ve null olabileceği belirtilmiştir.
    var Id:Int?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotViewHolder {//RecylerView de her bir item için ViewHolder oluşturur.
        val recycleRowBinding: RecyleRowBinding =
            RecyleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotViewHolder(recycleRowBinding)
    }
    override fun getItemCount(): Int {//Listedeki item sayısı kadar recylerView elemanı oluşturur.
        return notlistesi.size
    }

    override fun onBindViewHolder(holder: NotViewHolder, position: Int) {//Her bir item için ViewHolder a veriyi bağlar.
        holder.binding.textView6.text = notlistesi[position].baslik.toString()
        secilenNot=notlistesi[position]
        holder.itemView.setOnClickListener {//iteme tıklanırsa ilgili fragment e ıd si ile nota gönderir.
            val action = mainScreenDirections.actionMainScreen2ToNotsFragment(notlistesi[position].id, 0)
            Navigation.findNavController(it).navigate(action)
        }
        holder.itemView.setOnLongClickListener { //Basılı tutulduğunda ne olacağını yazdım.
           /* AlertDialog.Builder(holder.itemView.context) //İstersek burdan bir alert mesajı verilebilir fakat ben tasarım olarak pek güzel durmayacağını düşündüm.O yuzden yorum satırı.
                .setTitle("Notu Sil") //Uyarı mesajının başlığı
                .setMessage("Notu Silcenmi ?") //Sorusu
                .setPositiveButton("Evet") { dialog, which -> //Cevap pozitifse butonun texti ve yapacağı işlem
                    //Silme işlemi
                }
                .setNegativeButton("Hayır", null).show()
            true*/
            //showPopUpMenu(holder.itemView)
            holder.bind(notlistesi[position])//bind fonksiyonuna seçili olan notun bilgilerini gönderir.
            Id=notlistesi[position].id //Seçili olan notun id sini akılda tutmak için belirlediğimiz değişkene atıyoruz.
            true//uzun basıldımı sı true döner.Yapılacak işlemler buraya kadar gibisinden.
        }
    }

    private fun showPopUpMenu(view: View,notlar: Notlar) {//Popup menüyü gösterir.
        val popup = PopupMenu(view.context, view,0,0,R.style.CustomPopupMenu) //View den sonrası zevk için arka plan rengi için yazılmıştır.Yazmasanda olur.
        popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuitem -> //menuitem e tıklanırsa ne olucağını belirler.
            when (menuitem.itemId) {
                R.id.action_detele -> {
                    //Silme işlemi
                    listener.sil(notlar)//Interface deki fonksiyonu çağırır.
                    true
                }

                R.id.action_edit -> { //Belki ikinci bir işlem yapılmak istenirse. kullanmasanda olur.
                    //Düzenleme işlemi
                    listener.duzenle(Id!!)//Aklımızda tutmak için eklediğimiz id yi interfacedeki fonksiyona gönderiyoruz ordanda ilgili fragment e çekicez.istersek birden fazla fragment e de gönderebiliriz.
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
    inner class NotViewHolder(val binding: RecyleRowBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Notlar){
                showPopUpMenu(itemView,item)
        }
    }
}
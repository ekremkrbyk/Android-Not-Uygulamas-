package com.ekrem.karabiyik.loizanots.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.ekrem.karabiyik.loizanots.R
import com.ekrem.karabiyik.loizanots.adapter.NotsAdapter
import com.ekrem.karabiyik.loizanots.databinding.FragmentMainScreenBinding
import com.ekrem.karabiyik.loizanots.itemDeleteicin
import com.ekrem.karabiyik.loizanots.model.Notlar
import com.ekrem.karabiyik.loizanots.roomdb.NotDAO
import com.ekrem.karabiyik.loizanots.roomdb.NotDataBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class mainScreen : Fragment(),itemDeleteicin {
    private var _binding:FragmentMainScreenBinding? = null//view binding referansı bu oluşturulduğunda otomatik olarak FragmentMainScreenBinding sınıfı oluşturuluyor.
    private val binding get() = _binding!! //Güvenli binding sadece onCreateView oluşturulduğunda çalışır.
    //Veritabanı için gerekli
    private val mDisposable=CompositeDisposable()//Yaptığımız işlemlerin geçici olarak tutulduğu yer ram gibi ve uygulama kapanınca temizlenmesi gerekir yoksa uygulama kasar.
    private lateinit var db:NotDataBase //Room veritabanı ve database referansları iki satır.
    private lateinit var notDao :NotDAO

    override fun onCreate(savedInstanceState: Bundle?) {//Fragment oluşturulduğunda çağırılır veritabanını başlatır ve dao yu elde eder.
        super.onCreate(savedInstanceState)
        //DB ve DAO yu ekle
        db= Room.databaseBuilder(requireContext(),NotDataBase::class.java,"Not.data").build() //name kısmı telefonda oluşucak olan veritabanı dosyasının ismi.Kendin belirliyebilirsin fakat unutma!
        notDao=db.NotDao()
    }

    override fun onCreateView( //Fragmentin view ini oluşturur ve binding işlemini yapar.
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Binding ekle.
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {//View oluşturulduktan(Ekran geldikten) sonra FAB(FloatActionButton) u etkileşime geçirir tıklanınca yönlendirir.
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {//FAB a tıklanınca gerekli yönlendirmeyi yapar.
            val action=mainScreenDirections.actionMainScreen2ToNotsFragment(0,1)
            Navigation.findNavController(view).navigate(action)
        }
        //RecylerView için gerekli layout manager ayarını yapar.
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        //Veritabanından verileri sorgu ile alır ve recylerView e bağlar.
        verileriAl()
    }

    private fun verileriAl()//Veritabanından verileri sorgu ile alır ve handleResponse ile işleyip recylerView i günceller.
    {
        mDisposable.add(
            notDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(notlar:List<Notlar>)//Veritabanından gelen notlar listesini adapter e iletir ve recylerView i günceller.
    {
        val adapter=NotsAdapter(notlar,this)
        binding.recyclerView.adapter=adapter
    }

    override fun onDestroy() {//Fragment kapanınca devreye girer.
        super.onDestroy()
        _binding=null
        mDisposable.clear()
    }

    override fun sil(notlar: Notlar)//itemDeleteicin interface sini implement eder ve notları siler.
    {
        mDisposable.add(
            notDao.detele(notlar).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{//Buraya bu işlemler bittikten sonra ne yapılması isteniyorsa o yazılır.
                    verileriAl()//Silme işlemi tamamlandıktan sonra recylerView i günceller.
                }
        )
    }

    override fun duzenle(notId: Int) {//Pop up daki düzenle için hem yönlendiriyoruz hemde interface den gelen notId nin bilgilerini yansıtıyoruz.
        val action=mainScreenDirections.actionMainScreen2ToNotsFragment(notId,0)
        Navigation.findNavController(this.requireView()).navigate(action)
    }
}
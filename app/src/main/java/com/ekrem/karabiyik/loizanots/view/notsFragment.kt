package com.ekrem.karabiyik.loizanots.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.room.Room
import com.ekrem.karabiyik.loizanots.adapter.NotsAdapter
import com.ekrem.karabiyik.loizanots.databinding.FragmentNotsBinding
import com.ekrem.karabiyik.loizanots.model.Notlar
import com.ekrem.karabiyik.loizanots.roomdb.NotDAO
import com.ekrem.karabiyik.loizanots.roomdb.NotDataBase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private lateinit var db:NotDataBase
private lateinit var  notDao:NotDAO
private val mDisposable=CompositeDisposable()
private var secilenNot:Notlar?=null
class notsFragment : Fragment() {
    private var _binding:FragmentNotsBinding?=null
    private val binding get() =_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db=Room.databaseBuilder(requireContext(),NotDataBase::class.java,"Not.data").build()
        notDao= db.NotDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentNotsBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kydtBtn.setOnClickListener { kaydet(it) }
        arguments?.let {
            val bilgi=notsFragmentArgs.fromBundle(it).yeniMi
            if (bilgi==1)
            {
                //binding.kydtBtn.isEnabled=false
                binding.baslikText.setText("")
                binding.notText.setText("")
            }
            else
            {
                val id=notsFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    notDao.findByIdAraVeBul(id).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )
            }
        }
    }
    private fun handleResponse(notlar:Notlar)
    {
        secilenNot=notlar
        binding.baslikText.setText(notlar.baslik)
        binding.notText.setText(notlar.not)
    }
    fun kaydet(view: View)
    {
        val baslik=binding.baslikText.text.toString()
        val not=binding.notText.text.toString()
        val veri=Notlar(baslik,not)
        mDisposable.add(
            notDao.insert(veri).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForInsert)
        )
    }

    private fun handleResponseForInsert()
    {
        val action=notsFragmentDirections.actionNotsFragmentToMainScreen()
        Navigation.findNavController(requireView()).navigate(action)
    }
}
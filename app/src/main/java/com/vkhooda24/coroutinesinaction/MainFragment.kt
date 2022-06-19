package com.vkhooda24.coroutinesinaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkhooda24.coroutinesinaction.view.MainRecyclerViewAdapter

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModelRx: MainViewModelRx
    private lateinit var viewModelCoroutines: MainViewModelCoroutines

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.countriesListRecyclerView)
        val apiTypeText = view.findViewById<TextView>(R.id.apiTypeText)
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModelRx = ViewModelProvider(this)[MainViewModelRx::class.java]
        viewModelCoroutines = ViewModelProvider(this)[MainViewModelCoroutines::class.java]

//        makeApiCallsWithRx()
        makeApiCallsWithCoroutines()

        setRxAPIObserver(apiTypeText, recyclerView)
        setCoroutinesAPIObserver(apiTypeText, recyclerView)
    }

    private fun makeApiCallsWithRx() {
        viewModelRx.getCountriesListWithRx()
//        viewModelRx.getCountriesListSequentiallyWithRx()
//        viewModel.getCountriesListAsynchronouslyWithRx()
    }

    private fun makeApiCallsWithCoroutines() {
        viewModelCoroutines.getCountriesListWithCoroutines()
//        viewModelCoroutines.getCountriesListSequentiallyWithCoroutines()
//        viewModelCoroutines.getCountriesListAsynchronouslyWithCoroutines()
    }

    private fun setRxAPIObserver(
        apiTypeText: TextView,
        recyclerView: RecyclerView,
    ) {
        viewModelRx.countriesListLiveData.observe(viewLifecycleOwner) {
            apiTypeText.text = "Countries list with Rx API"
            val adapter = MainRecyclerViewAdapter(it)
            recyclerView.adapter = adapter
        }
    }

    private fun setCoroutinesAPIObserver(
        apiTypeText: TextView,
        recyclerView: RecyclerView,
    ) {
        viewModelCoroutines.countriesListLiveData.observe(viewLifecycleOwner) {
            apiTypeText.text = "Countries list with Coroutines API"
            val adapter = MainRecyclerViewAdapter(it)
            recyclerView.adapter = adapter
        }
    }
}


//        viewLifecycleOwner.lifecycleScope.launch {
//            val countriesList = viewModelCoroutines.getCountriesList()
//        }
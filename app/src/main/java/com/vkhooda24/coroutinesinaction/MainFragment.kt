package com.vkhooda24.coroutinesinaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkhooda24.coroutinesinaction.service.CountriesAPI
import com.vkhooda24.coroutinesinaction.service.RetrofitBuilder
import com.vkhooda24.coroutinesinaction.view.MainRecyclerViewAdapter


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val countriesAPI: CountriesAPI by lazy {
        RetrofitBuilder.getInstance().create(CountriesAPI::class.java)
    }

    private val viewModel: MainViewModel by lazy {
        MainViewModelCoroutinesImpl(MainRepoImpl(countriesAPI))
//        MainViewModelRxImpl(MainRepoImpl(countriesAPI))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[MainViewModelRxImpl::class.java]

        setCountriesListObserver()
        setCountryDetailsObserver()

        viewModel.getCountriesList()
        resetGuideline()
//        viewModel.getCountryDetails() //Sequential calls
//        viewModel.getCountriesListAndDetailsWithZipOperator("USA")
//        viewModel.getCountriesListAndDetailsAsynchronously("USA")
    }

    private fun setCountriesListObserver() {
        view?.let { view ->
            val recyclerView = view.findViewById<RecyclerView>(R.id.countriesListRecyclerView)
            val apiTypeText = view.findViewById<TextView>(R.id.apiTypeText)
            recyclerView.layoutManager = LinearLayoutManager(context)

            if (viewModel.countriesListLiveData.hasActiveObservers().not()) {
                viewModel.countriesListLiveData.observe(viewLifecycleOwner) {
                    val apiTextContent: String = apiTextContent()
                    apiTypeText.text = apiTextContent
                    recyclerView.visibility = View.VISIBLE
                    val adapter = MainRecyclerViewAdapter(it)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    private fun setCountryDetailsObserver() {
        view?.let { view ->
            val apiTypeText = view.findViewById<TextView>(R.id.apiTypeText)
            val countryNameText = view.findViewById<TextView>(R.id.countryName)
            val capitalNameText = view.findViewById<TextView>(R.id.countryCapitalName)
            val populationText = view.findViewById<TextView>(R.id.countryPopulation)
            val detailsGroup = view.findViewById<Group>(R.id.detailsGroup)

            if (viewModel.countryDetailsLiveData.hasActiveObservers().not()) {
                viewModel.countryDetailsLiveData.observe(viewLifecycleOwner) {
                    detailsGroup.visibility = View.VISIBLE
                    apiTypeText.text = apiTextContent()
                    countryNameText.text = getString(R.string.country_name, it.name.common)
                    capitalNameText.text =
                        getString(R.string.capital_name, it.capital.firstOrNull())
                    populationText.text = getString(R.string.population, it.population)
                }
            }
        }
    }

    private fun apiTextContent() =
        (viewModel is MainViewModelCoroutinesImpl).takeIf { isTrue -> isTrue }
            ?.let content@{
                return@content "Coroutines API Call's Result"
            } ?: "RxJava API Call's Result"

    private fun resetGuideline() {
        view?.let {
            val guideLine: Guideline = it.findViewById(R.id.topGuideline) as Guideline
            val params: ConstraintLayout.LayoutParams =
                guideLine.layoutParams as ConstraintLayout.LayoutParams
            params.guidePercent = 0.1f

            guideLine.layoutParams = params
        }
    }
}
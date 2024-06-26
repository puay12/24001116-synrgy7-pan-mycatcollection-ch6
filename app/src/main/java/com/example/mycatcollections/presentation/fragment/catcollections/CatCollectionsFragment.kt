package com.example.mycatcollections.presentation.fragment.catcollections

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mycatcollections.databinding.FragmentCatCollectionsBinding
import com.example.mycatcollections.extension.SpacesItemDecoration
import com.example.mycatcollections.presentation.fragment.catcollections.adapter.CatCollectionsAdapter
import com.example.mycatcollections.presentation.fragment.catcollections.adapter.CatCollectionsAdapterListener
import com.example.mycatcollections.presentation.viewmodel.CatCollectionsViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatCollectionsFragment : Fragment(), CatCollectionsAdapterListener {
    private val viewModel by viewModel<CatCollectionsViewModel>()
    private val catCollectionsAdapter = CatCollectionsAdapter(this)
    private lateinit var binding: FragmentCatCollectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCatCollectionsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData(view.context)
        refresh()
        binding.swipeRefresh.setOnRefreshListener { refresh() }
    }

    override fun onClickCard(id: String) {
        navigateToDetail(id)
    }

    private fun setData(context: Context) {
        binding.catList.layoutManager = GridLayoutManager(
            context,
            2,
        )
        binding.catList.adapter = catCollectionsAdapter
        binding.catList.itemAnimator = DefaultItemAnimator()
        binding.catList.addItemDecoration(SpacesItemDecoration(2,5,false))
    }

    private fun refresh() {
        viewModel.getLoading().observe(this) { isLoading ->
            if (isLoading) {
                binding.flipper.displayedChild = 1
            } else {
                binding.flipper.displayedChild = 0
            }
        }

        viewModel.getCatCollections().observe(this) { cats ->
            catCollectionsAdapter.submitList(cats)
        }

        viewModel.getError().observe(this) { error ->
            Snackbar.make(
                binding.root,
                error.message.toString(),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.swipeRefresh.isRefreshing = false
    }

    fun navigateToDetail(id: String) {
        val action = CatCollectionsFragmentDirections.actionCatCollectionsFragmentToCatDetailFragment(id)
        findNavController().navigate(action)
    }
}
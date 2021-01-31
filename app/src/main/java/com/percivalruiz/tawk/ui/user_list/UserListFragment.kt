package com.percivalruiz.tawk.ui.user_list

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.percivalruiz.tawk.R
import com.percivalruiz.tawk.databinding.FragmentUserListBinding
import com.percivalruiz.tawk.service.ConnectivityReceiver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import org.koin.android.ext.android.inject

class UserListFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private val viewModel by viewModel<UserListViewModel>()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val glide: RequestManager by inject()
    private lateinit var adapter: UserListAdapter
    private val connectivityReceiver = ConnectivityReceiver()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        requireActivity().registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        ConnectivityReceiver.connectivityReceiverListener = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasConnection()) {
            binding.noWifi.visibility = View.GONE
        } else {
            binding.noWifi.visibility = View.VISIBLE
        }

        adapter = UserListAdapter(glide) { id, login ->
            findNavController().navigate(
                UserListFragmentDirections.actionUserListFragmentToProfileFragment(
                    id,
                    login
                )
            )
        }

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingAdapter(adapter),
            footer = LoadingAdapter(adapter)
        )

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.users.collectLatest {
                if(adapter.itemCount != 0) {
                    binding.noWifi.visibility = View.GONE
                } else {
                    binding.list.visibility = View.VISIBLE
                }
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }

        binding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        binding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.search.text.toString())
                true
            }
            false
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasConnection(): Boolean {
        val cm =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(connectivityReceiver)
        _binding = null
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && binding.noWifi.visibility == View.VISIBLE) {
            adapter.refresh()
        }
    }
}
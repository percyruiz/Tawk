package com.percivalruiz.tawk.ui.user_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.percivalruiz.tawk.databinding.FragmentUserListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

class UserListFragment : Fragment() {

  private val viewModel by viewModel<UserListViewModel>()

  private var _binding: FragmentUserListBinding? = null
  private val binding get() = _binding!!
  private val adapter = UserListAdapter()

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentUserListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    view.findViewById<Button>(R.id.button_first).setOnClickListener {
//      findNavController().navigate(R.id.action_userListFragment_to_profileFragment)
//    }

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
        val test = it
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
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
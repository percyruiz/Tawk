package com.percivalruiz.tawk.ui.user_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.percivalruiz.tawk.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListFragment : Fragment() {

  private val viewModel by viewModel<UserListViewModel>()

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_user_list, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    view.findViewById<Button>(R.id.button_first).setOnClickListener {
      findNavController().navigate(R.id.action_userListFragment_to_profileFragment)
    }

    viewModel.userListFlow.asLiveData().observe(viewLifecycleOwner) {
      Log.d("test", it.toString())
    }

    viewModel.getUsers()

  }
}
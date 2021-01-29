package com.percivalruiz.tawk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.percivalruiz.tawk.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val args: ProfileFragmentArgs by navArgs()

    private val viewModel by viewModel<ProfileViewModel>()
    private val glide: RequestManager by inject()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar.title = args.login

        viewModel.getProfile(args.id, args.login)

        viewModel.profile.observe(viewLifecycleOwner) {

            binding.name.text = it.first.name
            binding.followersNo.text = it.first.followers.toString()
            binding.followingNo.text = it.first.following.toString()
            binding.company.text = it.first.company
            binding.blog.text = it.first.blog

            glide.load(it.first.avatarURL)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.avatar)

            binding.notes.setText(it.second?.content)

            binding.shimmerViewContainer.stopShimmerAnimation()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.content.visibility = View.VISIBLE
        }

        viewModel.noteSaveSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "note saved", Toast.LENGTH_SHORT).show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        binding.save.setOnClickListener {
            viewModel.saveNote(args.id, binding.notes.text.toString())
        }
    }


    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmerAnimation()
        binding.shimmerViewContainer.visibility = View.VISIBLE
    }

    override fun onPause() {
        binding.shimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
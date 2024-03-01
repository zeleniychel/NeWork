package ru.netology.nework.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentNewPostBinding
import ru.netology.nework.model.Post
import ru.netology.nework.util.getParcelableCompat
import ru.netology.nework.viewmodel.PostsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NewPostFragment : Fragment() {

    @Inject
    lateinit var appAuth: AppAuth
    private val viewModel by viewModels<PostsViewModel>()
    private val photoResultContract =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data ?: return@registerForActivityResult
                val file = uri.toFile()
                viewModel.setPhoto(uri, file)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val toolbar = (activity as MainActivity).findViewById<Toolbar>(R.id.topAppBar)
        toolbar.title = getString(R.string.new_post)
        toolbar.menu.clear()

        val binding = FragmentNewPostBinding.inflate(layoutInflater)
        val postArg = arguments?.getParcelableCompat<Post>("key")
        val resultListener = FragmentResultListener { requestKey ,result ->
            val list = result.getLongArray("list")?.toList()?: listOf()
            list.let { viewModel.setMentionIds(it) }
        }
        parentFragmentManager.setFragmentResultListener("key", viewLifecycleOwner, resultListener)


        viewModel.photo.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.remove.visibility = View.GONE
                return@observe
            }
            binding.remove.visibility = View.VISIBLE
            binding.imagePreview.setImageURI(it.uri)
            binding.imagePreview.visibility = View.VISIBLE
        }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.Builder(this)
                .crop()
                .galleryOnly()
                .maxResultSize(2048, 2048)
                .createIntent(photoResultContract::launch)
        }

        binding.editText.setText(postArg?.content)
        viewModel.edit(postArg?:Post())

        binding.remove.setOnClickListener {
            viewModel.setPhoto(null, null)
            binding.remove.visibility = View.GONE
            binding.imagePreview.visibility = View.GONE
        }

        binding.pickAttachment.setOnClickListener {

        }

        binding.choseMentioned.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_checkUsersFragment)
        }

        binding.makeMark.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_mapFragment)
        }

        toolbar.inflateMenu(R.menu.save_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save -> {
                    viewModel.save(binding.editText.text.toString())
                    findNavController().navigateUp()
                    true
                }

                else -> false
            }
        }



        return binding.root
    }
}
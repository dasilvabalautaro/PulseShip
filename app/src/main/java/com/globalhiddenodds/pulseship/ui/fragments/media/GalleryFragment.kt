package com.globalhiddenodds.pulseship.ui.fragments.media

import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.globalhiddenodds.pulseship.databinding.FragmentGalleryBinding
import com.globalhiddenodds.pulseship.ui.util.BitmapUtil
import com.globalhiddenodds.pulseship.ui.util.ManagerFile
import com.globalhiddenodds.pulseship.util.FaceRecognition
import com.globalhiddenodds.pulseship.ui.viewModel.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment: Fragment() {
    @Inject
    lateinit var managerFile: ManagerFile
    @Inject
    lateinit var bitmapUtil: BitmapUtil
    @Inject
    lateinit var faceRecognition: FaceRecognition

    private val galleryViewModel: GalleryViewModel by viewModels()
    private var _binding: FragmentGalleryBinding? = null
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryAdapter = GalleryAdapter { image ->
            binding.loading.visibility = View.VISIBLE
            selectedImage(image)
        }

        binding.gallery.also {
            it.layoutManager = GridLayoutManager(
                requireActivity(), 3)
            it.adapter = galleryAdapter
        }

        galleryViewModel.images.observe(this, { images ->
            binding.loading.visibility = View.GONE
            galleryAdapter.submitList(images)
        })

        binding.loading.visibility = View.VISIBLE
        showImages()
    }


    private fun selectedImage(image: MediaStoreImage){
        lifecycleScope.launch(Dispatchers.IO){
            val source = ImageDecoder.createSource(
                requireContext().contentResolver, image.contentUri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val bitmapResize = bitmapUtil.decodeBitmap(bitmap)
            val imageFace = faceRecognition.detectFace(bitmapResize)
            if (imageFace != null){
                managerFile.saveFaceImage(imageFace)
                activity!!.runOnUiThread {
                    binding.loading.visibility = View.GONE
                    findNavController().popBackStack()
                }
            }
            else{
                activity!!.runOnUiThread{
                    binding.loading.visibility = View.GONE
                    Toast.makeText(activity, "Face not found",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showImages() {
        galleryViewModel.loadImages()
    }
}
package com.globalhiddenodds.pulseship.ui.fragments.face

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentFaceBinding
import com.globalhiddenodds.pulseship.ui.activities.CameraActivity
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import com.globalhiddenodds.pulseship.util.FaceRecognition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class FaceFragment: BaseFragment() {
    @Inject
    lateinit var faceRecognition: FaceRecognition

    private val args: FaceFragmentArgs by navArgs()
    private var _binding: FragmentFaceBinding? = null
    private val binding get() = _binding!!
    private val filePhoto = "photo"
    override fun layoutId() = R.layout.fragment_face

    /** Flag indicating that there is depth data available for this image */
    //private val isDepth: Boolean by lazy { args.depth }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceBinding.inflate(
            inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bitmapUtil.orientation = args.orientation
        binding.btnOk.isEnabled = false
        binding.btnOk.setOnClickListener { (activity as CameraActivity).onBackPressed() }
        binding.btnRepeat.setOnClickListener { findNavController().popBackStack() }
        lifecycleScope.launch(Dispatchers.IO) {

            // Load input image file
            val inputBuffer = managerFile.loadInputBuffer(args.filePath)
            val bitMapResize = bitmapUtil.decodeBitmap(inputBuffer, inputBuffer.size)


            val bitMapToControl = faceRecognition.detectFace(bitMapResize)

            setImageToControl(bitMapToControl)
            saveFaceImage(bitMapToControl)

        }
    }

    private suspend fun saveFaceImage(bmp: Bitmap?){
        if (bmp != null){
            val scaleImage = Bitmap.createScaledBitmap(bmp,
                bmp.width / 2,
                bmp.height / 2, true)
            val output = managerFile.saveBitmap(
                requireContext(),
                scaleImage, filePhoto)
            pathFilePhoto = output.absolutePath

        }
    }

    private fun setImageToControl(bmp: Bitmap?){
        activity!!.runOnUiThread {
            if (bmp != null){
                binding.imageCapture.setImageBitmap(bmp)
                binding.btnOk.isEnabled = true
            }
            else{
                Toast.makeText(activity, "Face not found",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}
package com.globalhiddenodds.pulseship.ui.platform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.globalhiddenodds.pulseship.ui.util.BitmapUtil
import com.globalhiddenodds.pulseship.ui.util.EnableKeyboard
import com.globalhiddenodds.pulseship.ui.util.ManagerFile
import com.globalhiddenodds.pulseship.ui.viewModel.OwnerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment: Fragment() {

    companion object{
        var pathFilePhoto = ""
        //const val ANONYMOUS = "anonymous"
        //const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
    }

    protected val ownerViewModel: OwnerViewModel by activityViewModels()
    @Inject
    lateinit var enableKeyboard: EnableKeyboard
    @Inject lateinit var managerFile: ManagerFile
    @Inject lateinit var bitmapUtil: BitmapUtil

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutId(), container, false)

    internal fun notify(message: String){
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext,
            message, Toast.LENGTH_LONG).show()
    }

    internal fun notify(@StringRes resourceMessage: Int){
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext,
            resourceMessage, Toast.LENGTH_LONG).show()

    }
}
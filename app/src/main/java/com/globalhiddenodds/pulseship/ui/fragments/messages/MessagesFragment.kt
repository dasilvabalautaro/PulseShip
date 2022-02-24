package com.globalhiddenodds.pulseship.ui.fragments.messages

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentMessagesBinding
import com.globalhiddenodds.pulseship.datasource.network.data.MemberMessage
import com.globalhiddenodds.pulseship.datasource.network.data.Message
import com.globalhiddenodds.pulseship.ui.extensions.toBase64String
import com.globalhiddenodds.pulseship.ui.fragments.HomeFragmentArgs
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import com.globalhiddenodds.pulseship.ui.viewModel.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MessagesFragment: BaseFragment() {
    private val args: MessagesFragmentArgs by navArgs()
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!
    override fun layoutId() = R.layout.fragment_messages
    private val messageViewModel: MessageViewModel by viewModels()
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: MemberMessageAdapter
    private var nameOwner: String? = null
    private var photoOwner: String? = null
    private var contact: String? = null
    private val openDocument = registerForActivityResult(OpenDocumentContract()) { uri ->
        lifecycleScope.launch{
            onImageSelected(uri)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nameOwner = ownerViewModel.ownerCurrent.value!!.name
        photoOwner = ownerViewModel.ownerCurrent.value!!.photoB64
        newMember()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(
            inflater, container, false)
        //setHasOptionsMenu(true)
        observerViewModelResultUpdate()
        messageViewModel.listenerMessageEvent(ownerViewModel.uidCurrent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contact = args.contact
        messageViewModel.setUIDContact(contact!!)
        adapter = MemberMessageAdapter(nameOwner)
        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(requireContext())
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter
        messageViewModel.messagesWithContact.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }
        adapter.registerAdapterDataObserver(
            ScrollToBottomObserver(
                binding.messageRecyclerView,
                adapter,
                manager
            )
        )

        binding.messageEditText.addTextChangedListener(
            ButtonObserver(
                binding.sendButton
            )
        )

        binding.sendButton.setOnClickListener {
            val message = Message(
                binding.messageEditText.text.toString(),
                "",
                contact,
                ownerViewModel.uidCurrent,
                photoOwner,
                nameOwner
            )
            messageViewModel.sendMessage(
                contact!!, message)
            binding.messageEditText.setText("")

        }
        binding.addMessageImageView.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observerViewModelResultUpdate() {
        messageViewModel.taskResult.observe(viewLifecycleOwner,
            Observer { taskResult ->
                taskResult ?: return@Observer

                taskResult.error?.let {
                    showFailed(it)
                }
                taskResult.success?.let {
                    showSuccess(it)
                }
            }
        )
    }

    private fun showSuccess(message: String) {
        notify(message)
    }

    private fun showFailed(errorString: String) {
        notify(errorString)
    }

    private fun newMember(){
        val uid = ownerViewModel.uidCurrent
        val message = Message("", "", "", "", "", "")
        val memberMessage = MemberMessage( nameOwner, photoOwner, message)
        messageViewModel.newMember(uid, memberMessage)
    }

    private fun onImageSelected(uri: Uri) = runBlocking {
        var imgBase64 = ""

        val job = launch(Dispatchers.IO) {
            val source = ImageDecoder.createSource(
                requireContext().contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source)
            val bitmapResize = bitmapUtil.decodeBitmap(bitmap, 1)
            imgBase64 = bitmapResize.toBase64String()

        }
        job.join()

        val tempMessage = Message("", imgBase64,
            ownerViewModel.uidCurrent, ownerViewModel.uidCurrent,
            photoOwner, nameOwner)
        messageViewModel.sendMessage(ownerViewModel.uidCurrent, tempMessage)

    }

}
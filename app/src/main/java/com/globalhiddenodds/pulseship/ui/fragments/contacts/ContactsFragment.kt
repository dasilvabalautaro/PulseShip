package com.globalhiddenodds.pulseship.ui.fragments.contacts

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.databinding.FragmentContactsBinding
import com.globalhiddenodds.pulseship.ui.activities.MainActivity
import com.globalhiddenodds.pulseship.ui.platform.BaseFragment
import com.globalhiddenodds.pulseship.ui.viewModel.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment: BaseFragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    override fun layoutId() = R.layout.fragment_contacts
    private val messageViewModel: MessageViewModel by viewModels()
    private lateinit var manager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(
            inflater, container, false)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = SearchView(
            (requireActivity() as MainActivity)
                .supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                //binding.text.text = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //binding.text.text = newText
                return false
            }
        })

        searchView.setOnClickListener { view ->

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ContactsAdapter{
            val action = ContactsFragmentDirections.nextActionMessages(it.source)
            this.findNavController().navigate(action)
        }
        messageViewModel.setUIDCurrent(ownerViewModel.uidCurrent)
        manager = LinearLayoutManager(requireContext())
        binding.contactsRecyclerView.layoutManager = manager
        binding.contactsRecyclerView.adapter = adapter
        messageViewModel.newMessagesByContact.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

    }
}
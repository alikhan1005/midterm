import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.aviatickets.R
import com.example.aviatickets.adapter.OfferListAdapter
import com.example.aviatickets.databinding.FragmentOfferListBinding
import com.example.aviatickets.model.entity.Offer
import com.example.aviatickets.model.service.FakeService

import com.example.aviatickets.model.network.ApiClient
import kotlinx.coroutines.launch

class OfferListFragment : Fragment() {

    companion object {
        fun newInstance() = OfferListFragment()
    }

    private var _binding: FragmentOfferListBinding? = null
    private val binding get() = _binding!!

    private val adapter: OfferListAdapter by lazy {
        OfferListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        fetchOffers()
    }

    private fun setupUI() {
        binding.offerList.adapter = adapter

        binding.sortRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sort_by_price -> {
                    val sortedByPrice = FakeService.offerList.sortedBy { it.price }
                    adapter.updateItems(sortedByPrice)
                }
                R.id.sort_by_duration -> {
                    val sortedByDuration = FakeService.offerList.sortedBy { it.flight.duration }
                    adapter.updateItems(sortedByDuration)
                }
            }
        }
    }


    private fun fetchOffers() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getOfferList()
                if (response.isSuccessful) {
                    val offers = response.body() ?: emptyList()
                    adapter.updateItems(offers)
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network error or exception
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

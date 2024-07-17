import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.Data.mealsR
import com.example.foodapp.databinding.SearchFragmentBinding
import com.example.foodapp.ui.Home.HomeModel
import com.example.foodapp.ui.Search.SearchAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var adapter: SearchAdapter
    private val viewModel: HomeModel by viewModels()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val mealItems = mutableListOf<mealsR>()
    private val filteredMealsItems = mutableListOf<mealsR>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth
        database = FirebaseDatabase.getInstance().reference // Initialize FirebaseDatabase reference

        binding.BackArrow.setOnClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToHomeFragment()
            findNavController().navigate(action)
        }

        // Set up the RecyclerView
        adapter = SearchAdapter()
        binding.RecentSearch.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.RecentSearch.adapter = adapter

        // Set up search text change listener
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateFilteredRadios(s.toString())
            }
        })

        // Observe ViewModel for initial data load
        observeViewModel()

        // Read user data from Firebase
        readData()
    }

    private fun updateFilteredRadios(query: String) {
        filteredMealsItems.clear()
        for (item in mealItems) {
            if (item.strMeal?.contains(query, ignoreCase = true) == true) {
                filteredMealsItems.add(item)
            }
        }
        adapter.meals = filteredMealsItems
        adapter.notifyDataSetChanged()
    }

    private fun readData() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.e(TAG, "User not authenticated")
            return
        }

        val uidRef = database.child("users").child(uid)

        uidRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val favoritesList = mutableListOf<String>()
                    favoritesList.clear()

                    val data = snapshot.child("favorites").getValue(object : GenericTypeIndicator<List<String>>() {})
                    data?.let {
                        favoritesList.addAll(it)
                    }


                    for (id in favoritesList) {
                        viewModel.getFavoriteById(id) // Assuming `id` is a String
                    }

                } else {
                    Log.d(TAG, "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error reading data", error.toException())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.favoriteMeals.observe(viewLifecycleOwner, Observer { meals ->
            meals?.let {
                mealItems.clear()
                mealItems.addAll(it)
                updateFilteredRadios("")
            }
        })
    }
    override fun onStop() {
        super.onStop()
        viewModel.clearFavoriteMeals()

    }

    override fun onPause() {
        super.onPause()
        viewModel.clearFavoriteMeals()

    }

}

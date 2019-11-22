package com.krshubham.mealsonwheels.ui.fragments.Food
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.krshubham.mealsonwheels.R.layout.fragment_food

class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        foodViewModel =
            ViewModelProviders.of(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(fragment_food, container, false)
        return root

    }

}
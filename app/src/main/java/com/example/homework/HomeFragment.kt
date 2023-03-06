package com.example.homework

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homework.databinding.FragmentHomeBinding
import com.example.homework.hw2.GradeCalcActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.intToText.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_FirstFragment)
        }
        binding.gradeCalculator.setOnClickListener {
            val intent = Intent(requireActivity(), GradeCalcActivity::class.java)
            startActivity(intent)
        }
    }
}
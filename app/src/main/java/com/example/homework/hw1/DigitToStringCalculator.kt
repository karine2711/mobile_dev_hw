package com.example.homework.hw1

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.homework.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DigitToStringCalculator : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val digitStr = binding.number.text.toString()
            if (digitStr.length > 6) {
                showAlertBox("Number is bigger than 999999!")
                return@setOnClickListener
            }
            val number = digitStr.toInt()
            if (number < 0) {
                showAlertBox("Number is negative!")
            }
            val answer = IntegerToTextUtil.constructString(number)
            binding.answerTextbox.text = answer
        }
    }

    private fun showAlertBox(msg: String) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            context
        )
        alertDialogBuilder.setTitle("Invalid number")
        alertDialogBuilder
            .setMessage(msg)
            .setCancelable(true)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
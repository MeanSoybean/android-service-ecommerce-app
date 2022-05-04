package com.example.androiddevelopmentgroup7.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androiddevelopmentgroup7.R
import com.example.androiddevelopmentgroup7.views.activities.SignInActivity
import com.google.android.material.textfield.TextInputEditText

interface ICustomerSignUp {
    fun signUpCustomer(name: String, email: String, password: String)
}

/**
 * A simple [Fragment] subclass.
 */
class SignUpCustomerFragment : Fragment() {
    private lateinit var nameET: TextInputEditText
    private lateinit var emailET: TextInputEditText
    private lateinit var passwordET: TextInputEditText
    private lateinit var confPasswordET: TextInputEditText
    private lateinit var dataPasser: ICustomerSignUp

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as ICustomerSignUp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEditTexts(view)
        setupButtons(view)
    }

    private fun setupEditTexts(view: View) {
        nameET = view.findViewById(R.id.signup_name_edit_text)
        emailET = view.findViewById(R.id.signup_email_edit_text)
        passwordET = view.findViewById(R.id.signup_password_edit_text)
        confPasswordET = view.findViewById(R.id.signup_conf_password_edit_text)
    }

    private fun setupButtons(view: View) {
        view.findViewById<TextView>(R.id.signup_cancel_button).setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }
        view.findViewById<Button>(R.id.vendor_signup_switch_button).setOnClickListener {
            findNavController().navigate(R.id.action_signUpCustomerFragment_to_signUpVendorFragment)
        }
        view.findViewById<Button>(R.id.signup_confirm_button).setOnClickListener {
            onSignupConfirm()
        }
    }

    private fun onSignupConfirm() {
        // Perform data validation'
        if (!isDataValid()) {
            Toast.makeText(activity, "Bad input data!", Toast.LENGTH_SHORT).show()
            return
        }
        // Send data back to the SignUpActivity
        val name = nameET.text.toString()
        val email = emailET.text.toString()
        val password = passwordET.text.toString()
        dataPasser.signUpCustomer(name, email, password)
    }

    private fun isDataValid(): Boolean {
        val name = nameET.text.toString()
        val email = emailET.text.toString()
        val pass1 = passwordET.text.toString()
        val pass2 = confPasswordET.text.toString()
        return name.isNotBlank()
                && email.isNotBlank()
                && (pass1 == pass2)
                && pass1.isNotBlank()
    }
}
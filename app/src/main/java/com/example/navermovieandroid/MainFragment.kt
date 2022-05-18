package com.example.navermovieandroid

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout


class MainFragment : Fragment() {

    private lateinit var inputLayout: TextInputLayout
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        // editText에서 enter키를 입력받았을 경우
        editText.setOnKeyListener(object :View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    // testcode
                    Toast.makeText(requireContext(), editText.text, Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
    }

    private fun initView(view: View){
        inputLayout = view.findViewById(R.id.tiLayout)
        editText = inputLayout.editText!!
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
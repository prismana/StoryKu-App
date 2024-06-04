package com.prismana.storyku.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputEditText

class EditTextPasswordKu @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length in 1..7) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

        })

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hint = "Masukkan Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


}
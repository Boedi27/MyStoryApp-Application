package com.example.mystoryapp.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class MyPasswordEditText : AppCompatEditText {

    private lateinit var passwordIcImage: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        passwordIcImage = ContextCompat.getDrawable(context, R.drawable.ic_lock_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do Nothing
                setIcButtonDrawables()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isValidPassword(text.toString())) {
                    error = resources.getString(R.string.invalid_password)
                    setIcButtonDrawables()
                } else {
                    setIcButtonDrawables()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Do Nothing
                setIcButtonDrawables()
            }

        })
    }

    private fun setIcButtonDrawables(
        startOfTheText: Drawable = passwordIcImage,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun isValidPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password) && text.toString().length >= 8
    }
}
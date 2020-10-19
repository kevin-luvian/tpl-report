package com.example.app4_fragment.viewmodels.model

import androidx.lifecycle.ViewModel

class StorageViewModel : ViewModel() {
    private var textData = String()
    private var contentData = String()

    fun setTextData(text:String){
        textData = text
    }

    fun getTextData(): String? {
        return textData
    }

    fun setContentData(text:String){
        contentData = text
    }

    fun getContentData(): String? {
        return contentData
    }
}
package com.hakamada.explorer2.reader

import java.util.Arrays

class Tes {

    private var array: List<String>? = null

    fun getArray(): List<String>? {
        if (array == null) {
            array = Arrays.asList("ok", "not ok")
        }
        return array
    }
}

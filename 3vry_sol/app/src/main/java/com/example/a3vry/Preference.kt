package com.example.a3vry

class Preference {
    var id: Int = 0
    var parameter: String = ""
    var value: String = ""

    constructor(parameter: String, value: String) {
        this.parameter = parameter
        this.value = value
    }
}
package com.example.a3vry

class Song {
    var id: Int = 0
    var title: String = ""
    var dateTime: String = ""
    var url: String = ""
    var bandId: Int = 0

    constructor(title: String, dateTime: String, url: String, bandId: Int) {
        this.title = title
        this.dateTime = dateTime
        this.url = url
        this.bandId = bandId
    }
}
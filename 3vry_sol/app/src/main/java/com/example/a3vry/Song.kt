package com.example.a3vry

class Song {
    var id: Int = 0
    var title: String = ""
    var dateTime: String = ""
    var url: String = ""
    var artistId: Int = 0

    constructor(title: String, dateTime: String, url: String, artistId: Int) {
        this.title = title
        this.dateTime = dateTime
        this.url = url
        this.artistId = artistId
    }
}
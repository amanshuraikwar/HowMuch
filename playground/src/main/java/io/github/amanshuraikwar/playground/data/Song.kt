package io.github.amanshuraikwar.playground.data

import java.util.*

data class Song(val timeAdded: Date,
                val name: String,
                val artist: String,
                val album: String,
                val artUrl: String,
                val spotifyUrl: String)
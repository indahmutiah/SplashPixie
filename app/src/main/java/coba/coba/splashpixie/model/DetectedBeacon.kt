package coba.coba.splashpixie.model

class DetectedBeacon {
    var rssi: Int = 0
    var distance: Double? = null
    var major: Int = 0
    var minor: Int = 0
    var code: String? = null

    var bookshelf: Bookshelf? = null
    var lastDetected: Long? = null
    val uuid: Int = 0
}

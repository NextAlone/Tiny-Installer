package dev.trindadedev.tinyinstaller.data.app.model.exception

class InstallFailedInvalidInstallLocationException : Exception {
    constructor() : super()

    constructor(message: String?) : super(message)
}
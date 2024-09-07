package dev.trindadedev.tinyinstaller.data.recycle.model.exception

class DhizukuNotWorkException : RuntimeException {
    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

package com.zandraa.migrationhelper.utils

import com.intellij.openapi.util.TextRange

private const val UUID_CHARS_LOWER = "0123456789abcdef"
private const val UUID_CHARS_UPPER = "0123456789ABCDEF"

private val FIND_UUID_LONG_REGEX =
        """(?<![0-9a-zA-Z])(([${UUID_CHARS_LOWER}]{8})-([${UUID_CHARS_LOWER}]{4})-([${UUID_CHARS_LOWER}]{4})-([${UUID_CHARS_LOWER}]{4})-([${UUID_CHARS_LOWER}]{12})|([${UUID_CHARS_UPPER}]{8})-([${UUID_CHARS_UPPER}]{4})-([${UUID_CHARS_UPPER}]{4})-([${UUID_CHARS_UPPER}]{4})-([${UUID_CHARS_UPPER}]{12}))(?!([0-9a-zA-Z]|\())""".toRegex()

fun CharSequence.findUUIDs() = FIND_UUID_LONG_REGEX.findAll(this)
        .map { it.value to it.range }

fun IntRange.textRange(offset: Int = 0) = this.let {
    TextRange(
            offset + it.first,
            offset + it.last + 1
    )
}
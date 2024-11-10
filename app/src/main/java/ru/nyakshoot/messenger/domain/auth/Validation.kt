package ru.nyakshoot.messenger.domain.auth

import java.util.regex.Pattern

class Validation {

    companion object {
        private const val MINIMAL_PASSWORD_LENGTH = 6
        private const val MINIMAL_USERNAME_LENGTH = 2


        fun validEmail(value: String?): String? {
            val pattern = Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            )
            return if (!value.isNullOrBlank()
                && pattern.matcher(value).matches()
            ) {
                value
            } else {
                null
            }
        }

        fun validPassword(value: String?): String? =
            if (!value.isNullOrBlank()
                && (value.length >= Companion.MINIMAL_PASSWORD_LENGTH)
            ) {
                value
            } else {
                null
            }

        fun validUsername(value: String?): String? =
            if (!value.isNullOrBlank()
                && (value.length >= Companion.MINIMAL_USERNAME_LENGTH)
            ) {
                value
            } else {
                null
            }

    }
}
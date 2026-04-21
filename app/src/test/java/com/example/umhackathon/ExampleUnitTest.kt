package com.example.umhackathon

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun word_count_filter() {
        val input = "test"
        val chaCount = input.length

        // We expect this to be false because our limit was 1 (for easier testing)
        assert(chaCount > 1)
    }

    @Test
    fun input_check() {
        val input = ""
        // We expect this to be false because input is empty
        assert(input.isEmpty())
    }
}
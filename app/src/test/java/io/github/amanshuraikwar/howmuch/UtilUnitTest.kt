package io.github.amanshuraikwar.howmuch

import io.github.amanshuraikwar.howmuch.base.util.Util;
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilUnitTest {
    @Test
    fun curMonth_isCorrect() {
        assertEquals("September", Util.getCurMonth())
    }

    @Test
    fun curDateTime_isCorrect() {
        System.out.println(Util.getCurDateTime())
    }

    @Test
    fun rowIndex_isCorrect() {
        assertEquals(34, Util.getRowNumber("Transactions!AAA34:BCD"))
    }
}

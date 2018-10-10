package io.github.amanshuraikwar.howmuch.util

import java.util.*

/**
 * Taken from: https://github.com/midorikocak/currency-picker-android
 */
class ExtendedCurrency(val code: String,
                       val name: String,
                       val symbol: String) {

    class NameComparator : Comparator<ExtendedCurrency> {

        override fun compare(country: ExtendedCurrency, t1: ExtendedCurrency): Int {
            return country.name.compareTo(t1.name)
        }
    }

    class ISOCodeComparator : Comparator<ExtendedCurrency> {

        override fun compare(currency: ExtendedCurrency, t1: ExtendedCurrency): Int {
            return currency.code.compareTo(t1.code)
        }
    }

    companion object {

        private val CURRENCIES: Array<ExtendedCurrency> = arrayOf(ExtendedCurrency("EUR", "Euro", "€"), ExtendedCurrency("USD", "United States Dollar", "$"), ExtendedCurrency("GBP", "British Pound", "£"), ExtendedCurrency("CZK", "Czech Koruna", "Kč"), ExtendedCurrency("TRY", "Turkish Lira", "₺"), ExtendedCurrency("JPY", "Japanese Yen", "¥"), ExtendedCurrency("AED", "Emirati Dirham", "د.إ"), ExtendedCurrency("AFN", "Afghanistan Afghani", "؋"), ExtendedCurrency("ARS", "Argentine Peso", "$"), ExtendedCurrency("AUD", "Australian Dollar", "$"), ExtendedCurrency("BBD", "Barbados Dollar", "$"), ExtendedCurrency("BDT", "Bangladeshi Taka", " Tk"), ExtendedCurrency("BGN", "Bulgarian Lev", "лв"), ExtendedCurrency("BHD", "Bahraini Dinar", "BD"), ExtendedCurrency("BMD", "Bermuda Dollar", "$"), ExtendedCurrency("BND", "Brunei Darussalam Dollar", "$"), ExtendedCurrency("BOB", "Bolivia Bolíviano", "\$b"), ExtendedCurrency("BRL", "Brazil Real", "R$"), ExtendedCurrency("BTN", "Bhutanese Ngultrum", "Nu."), ExtendedCurrency("BZD", "Belize Dollar", "BZ$"), ExtendedCurrency("CAD", "Canada Dollar", "$"), ExtendedCurrency("CHF", "Switzerland Franc", "CHF"), ExtendedCurrency("CLP", "Chile Peso", "$"), ExtendedCurrency("CNY", "China Yuan Renminbi", "¥"), ExtendedCurrency("COP", "Colombia Peso", "$"), ExtendedCurrency("CRC", "Costa Rica Colon", "₡"), ExtendedCurrency("DKK", "Denmark Krone", "kr"), ExtendedCurrency("DOP", "Dominican Republic Peso", "RD$"), ExtendedCurrency("EGP", "Egypt Pound", "£"), ExtendedCurrency("ETB", "Ethiopian Birr", "Br"), ExtendedCurrency("GEL", "Georgian Lari", "\u20be"), ExtendedCurrency("GHS", "Ghana Cedi", "¢"), ExtendedCurrency("GMD", "Gambian dalasi", "D"), ExtendedCurrency("GYD", "Guyana Dollar", "$"), ExtendedCurrency("HKD", "Hong Kong Dollar", "$"), ExtendedCurrency("HRK", "Croatia Kuna", "kn"), ExtendedCurrency("HUF", "Hungary Forint", "Ft"), ExtendedCurrency("IDR", "Indonesia Rupiah", "Rp"), ExtendedCurrency("ILS", "Israel Shekel", "₪"), ExtendedCurrency("INR", "India Rupee", "₹"), ExtendedCurrency("ISK", "Iceland Krona", "kr"), ExtendedCurrency("JMD", "Jamaica Dollar", "J$"), ExtendedCurrency("JPY", "Japan Yen", "¥"), ExtendedCurrency("KES", "Kenyan Shilling", "KSh"), ExtendedCurrency("KRW", "Korea (South) Won", "₩"), ExtendedCurrency("KWD", "#N/A", "#N/A"), ExtendedCurrency("KYD", "Cayman Islands Dollar", "$"), ExtendedCurrency("KZT", "Kazakhstan Tenge", "лв"), ExtendedCurrency("LAK", "Laos Kip", "₭"), ExtendedCurrency("LKR", "Sri Lanka Rupee", "₨"), ExtendedCurrency("LRD", "Liberia Dollar", "$"), ExtendedCurrency("LTL", "Lithuanian Litas", "Lt"), ExtendedCurrency("MAD", "Moroccan Dirham", "MAD"), ExtendedCurrency("MDL", "Moldovan Leu", "MDL"), ExtendedCurrency("MKD", "Macedonia Denar", "ден"), ExtendedCurrency("MNT", "Mongolia Tughrik", "₮"), ExtendedCurrency("MUR", "Mauritius Rupee", "₨"), ExtendedCurrency("MWK", "Malawian Kwacha", "MK"), ExtendedCurrency("MXN", "Mexico Peso", "$"), ExtendedCurrency("MYR", "Malaysia Ringgit", "RM"), ExtendedCurrency("MZN", "Mozambique Metical", "MT"), ExtendedCurrency("NAD", "Namibia Dollar", "$"), ExtendedCurrency("NGN", "Nigeria Naira", "₦"), ExtendedCurrency("NIO", "Nicaragua Cordoba", "C$"), ExtendedCurrency("NOK", "Norway Krone", "kr"), ExtendedCurrency("NPR", "Nepal Rupee", "₨"), ExtendedCurrency("NZD", "New Zealand Dollar", "$"), ExtendedCurrency("OMR", "Oman Rial", "﷼"), ExtendedCurrency("PEN", "Peru Sol", "S/."), ExtendedCurrency("PGK", "Papua New Guinean Kina", "K"), ExtendedCurrency("PHP", "Philippines Peso", "₱"), ExtendedCurrency("PKR", "Pakistan Rupee", "₨"), ExtendedCurrency("PLN", "Poland Zloty", "zł"), ExtendedCurrency("PYG", "Paraguay Guarani", "Gs"), ExtendedCurrency("QAR", "Qatar Riyal", "﷼"), ExtendedCurrency("RON", "Romania Leu", "lei"), ExtendedCurrency("RSD", "Serbia Dinar", "Дин."), ExtendedCurrency("RUB", "Russia Ruble", "\u20bd"), ExtendedCurrency("SAR", "Saudi Arabia Riyal", "﷼"), ExtendedCurrency("SEK", "Sweden Krona", "kr"), ExtendedCurrency("SGD", "Singapore Dollar", "$"), ExtendedCurrency("SOS", "Somalia Shilling", "S"), ExtendedCurrency("SRD", "Suriname Dollar", "$"), ExtendedCurrency("THB", "Thailand Baht", "฿"), ExtendedCurrency("TTD", "Trinidad and Tobago Dollar", "TT$"), ExtendedCurrency("TWD", "Taiwan New Dollar", "NT$"), ExtendedCurrency("TZS", "Tanzanian Shilling", "TSh"), ExtendedCurrency("UAH", "Ukraine Hryvnia", "₴"), ExtendedCurrency("UGX", "Ugandan Shilling", "USh"), ExtendedCurrency("UYU", "Uruguay Peso", "\$U"), ExtendedCurrency("VEF", "Venezuela Bolívar", "Bs"), ExtendedCurrency("VND", "Viet Nam Dong", "₫"), ExtendedCurrency("YER", "Yemen Rial", "﷼"), ExtendedCurrency("ZAR", "South Africa Rand", "R"))

        fun getCurrencyByISO(currencyIsoCode: String): ExtendedCurrency? {
            val c = ExtendedCurrency(currencyIsoCode.toUpperCase(), "", "")
            val i = Arrays.binarySearch(CURRENCIES, c, ExtendedCurrency.ISOCodeComparator())
            return if (i < 0) null else CURRENCIES[i]
        }

        fun getCurrencyByName(currencyName: String): ExtendedCurrency? {
            return CURRENCIES.find { it.name == currencyName }
        }

        fun getAll() = CURRENCIES
    }
}
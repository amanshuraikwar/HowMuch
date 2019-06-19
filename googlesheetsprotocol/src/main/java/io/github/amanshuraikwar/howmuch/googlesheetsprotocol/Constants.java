package io.github.amanshuraikwar.howmuch.googlesheetsprotocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Note:
 *      1. Sheet cell range format: sheet_title ! start_col start_row_num : end_col
 *
 * @author amanshuraikwar
 */
public interface Constants {

    String METADATA_SHEET_TITLE = "Metadata";

    //region Categories

    int CATEGORIES_START_ROW_WITH_HEADING = 2;
    int CATEGORIES_START_ROW_WITHOUT_HEADING = 4;

    String CATEGORIES_START_COL = "B";
    String CATEGORIES_END_COL = "E";

    String CATEGORIES_CELL_RANGE_WITH_HEADING =
            CATEGORIES_START_COL + CATEGORIES_START_ROW_WITH_HEADING + ":" + CATEGORIES_END_COL;

    String CATEGORIES_CELL_RANGE_WITHOUT_HEADING =
            CATEGORIES_START_COL + CATEGORIES_START_ROW_WITHOUT_HEADING + ":" + CATEGORIES_END_COL;

    String CATEGORIES_SPREAD_SHEET_RANGE_WITH_HEADING =
            METADATA_SHEET_TITLE + "!" + CATEGORIES_CELL_RANGE_WITH_HEADING;

    String CATEGORIES_SPREAD_SHEET_RANGE_WITHOUT_HEADING =
            METADATA_SHEET_TITLE + "!" + CATEGORIES_CELL_RANGE_WITHOUT_HEADING;

    List<List<String>> DEFAULT_CATEGORIES_WITH_HEADING = Arrays.asList(
            Collections.singletonList("Categories"),// 2
                          // B              // C        // D        // E
            Arrays.asList("Name",           "Type",     "Active",   "MonthlyLimit"),// 3
            Arrays.asList("Food",           "DEBIT",    "true",     "1000.00"), // 4
            Arrays.asList("Health/Medical", "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Home",           "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Transportation", "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Personal",       "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Utilities",      "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Travel",         "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Debt",           "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Other",          "DEBIT",    "true",     "1000.00"),
            Arrays.asList("Savings",        "CREDIT",   "true",     "1000.00"),
            Arrays.asList("Paycheck",       "CREDIT",   "true",     "1000.00"),
            Arrays.asList("Bonus",          "CREDIT",   "true",     "1000.00"),
            Arrays.asList("Interest",       "CREDIT",   "true",     "1000.00"),
            Arrays.asList("Other",          "CREDIT",   "true",     "1000.00")
    );

    //endregion

    //region Wallets

    int WALLETS_START_ROW_WITH_HEADING = 2;
    int WALLETS_START_ROW_WITHOUT_HEADING = 4;

    String WALLETS_START_COL = "F";
    String WALLETS_END_COL = "H";

    String WALLETS_CELL_RANGE_WITH_HEADING =
            WALLETS_START_COL + WALLETS_START_ROW_WITH_HEADING + ":" + WALLETS_END_COL;

    String WALLETS_CELL_RANGE_WITHOUT_HEADING =
            WALLETS_START_COL + WALLETS_START_ROW_WITHOUT_HEADING + ":" + WALLETS_END_COL;

    String WALLETS_SPREAD_SHEET_RANGE_WITH_HEADING =
            METADATA_SHEET_TITLE + "!" + WALLETS_CELL_RANGE_WITH_HEADING;

    String WALLETS_SPREAD_SHEET_RANGE_WITHOUT_HEADING =
            METADATA_SHEET_TITLE + "!" + WALLETS_CELL_RANGE_WITHOUT_HEADING;

    List<List<String>> DEFAULT_WALLETS_WITH_HEADING = Arrays.asList(
            Collections.singletonList("Wallets"),// 2
                            // F            // G        // H
            Arrays.asList(  "Name",         "Balance",  "Active"),// 3
            Arrays.asList(  "Corporate",    "0.0",      "true"),// 4
            Arrays.asList(  "Personal",     "0.0",      "true"),// 5
            Arrays.asList(  "Other",        "0.0",      "true")// 6
    );

    //endregion

    String TRANSACTIONS_SHEET_TITLE = "Transactions-1";

    //region Transactions

    int TRANSACTION_START_ROW_WITH_HEADING = 2;
    int TRANSACTION_START_ROW_WITHOUT_HEADING = 4;

    String TRANSACTION_START_COL = "B";
    String TRANSACTION_END_COL = "I";

    int TRANSACTION_ROW_COLUMN_COUNT = 8;

    String TRANSACTIONS_CELL_RANGE_WITH_HEADING =
            TRANSACTION_START_COL + TRANSACTION_START_ROW_WITH_HEADING + ":" + TRANSACTION_END_COL;

    String TRANSACTIONS_CELL_RANGE_WITHOUT_HEADING =
            TRANSACTION_START_COL + TRANSACTION_START_ROW_WITHOUT_HEADING + ":" + TRANSACTION_END_COL;

    List<List<String>> TRANSACTIONS_HEADING = Arrays.asList(
            Collections.singletonList("Transactions"),
                Arrays.asList(
                        "Date",
                        "Time",
                        "Amount",
                        "Title",
                        "Description",
                        "CategoryId",
                        "Type",
                        "WalletId"
                )
    );

    //endregion
}

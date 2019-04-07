package io.github.amanshuraikwar.howmuch.googlesheetsprotocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constants {

    String METADATA_SHEET_TITLE = "Metadata";

    //region Categories

    int CATEGORIES_START_ROW_WITH_HEADING = 2;
    int CATEGORIES_START_ROW_WITHOUT_HEADING = 4;

    String CATEGORIES_START_COL = "B";
    String CATEGORIES_END_COL = "C";

    String CATEGORIES_CELL_RANGE_WITH_HEADING =
            CATEGORIES_START_COL + CATEGORIES_START_ROW_WITH_HEADING + ":" + CATEGORIES_END_COL;

    String CATEGORIES_CELL_RANGE_WITHOUT_HEADING =
            CATEGORIES_START_COL + CATEGORIES_START_ROW_WITHOUT_HEADING + ":" + CATEGORIES_END_COL;

    List<List<String>> DEFAULT_CATEGORIES_WITH_HEADING = Arrays.asList(
            Collections.singletonList("Categories"),
            Arrays.asList("Name", "Type"),
            Arrays.asList("Food", "DEBIT"),
            Arrays.asList("Health/Medical", "DEBIT"),
            Arrays.asList("Home", "DEBIT"),
            Arrays.asList("Transportation", "DEBIT"),
            Arrays.asList("Personal", "DEBIT"),
            Arrays.asList("Utilities", "DEBIT"),
            Arrays.asList("Travel", "DEBIT"),
            Arrays.asList("Debt", "DEBIT"),
            Arrays.asList("Other", "DEBIT"),
            Arrays.asList("Savings", "CREDIT"),
            Arrays.asList("Paycheck", "CREDIT"),
            Arrays.asList("Bonus", "CREDIT"),
            Arrays.asList("Interest", "CREDIT"),
            Arrays.asList("Other", "CREDIT")
    );

    //endregion

    //region Wallets

    int WALLETS_START_ROW_WITH_HEADING = 2;
    int WALLETS_START_ROW_WITHOUT_HEADING = 4;

    String WALLETS_START_COL = "F";
    String WALLETS_END_COL = "G";

    String WALLETS_CELL_RANGE_WITH_HEADING =
            WALLETS_START_COL + WALLETS_START_ROW_WITH_HEADING + ":" + WALLETS_END_COL;

    String WALLETS_CELL_RANGE_WITHOUT_HEADING =
            WALLETS_START_COL + WALLETS_START_ROW_WITHOUT_HEADING + ":" + WALLETS_END_COL;

    List<List<String>> DEFAULT_WALLETS_WITH_HEADING = Arrays.asList(
            Collections.singletonList("Wallets"),
            Arrays.asList("Name", "Balance"),
            Arrays.asList("Primary", "0.0")
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

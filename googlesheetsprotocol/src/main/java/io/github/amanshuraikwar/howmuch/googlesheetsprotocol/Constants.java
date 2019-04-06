package io.github.amanshuraikwar.howmuch.googlesheetsprotocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constants {

    String METADATA_SHEET_TITLE = "Metadata";
    String TRANSACTIONS_SHEET_TITLE = "Transactions-1";

    String CATEGORIES_CELL_RANGE_WITH_HEADING = "B2:B";
    String CATEGORIES_CELL_RANGE_WITHOUT_HEADING = "B3:B";

    List<List<String>> DEFAULT_CATEGORIES_WITH_HEADING = Arrays.asList(
            Collections.singletonList("Categories"),
            Collections.singletonList("Food"),
            Collections.singletonList("Health/Medical"),
            Collections.singletonList("Home"),
            Collections.singletonList("Transportation"),
            Collections.singletonList("Personal"),
            Collections.singletonList("Utilities"),
            Collections.singletonList("Travel"),
            Collections.singletonList("Debt"),
            Collections.singletonList("Other")
    );

    List<List<String>> TRANSACTIONS_HEADING = Arrays.asList(
            Collections.singletonList("Transactions"),
                Arrays.asList(
                        "Date", "Time", "Amount", "Title", "Description", "Category", "Type"
                )
    );

    String TRANSACTIONS_CELL_RANGE_WITH_HEADING = "B2:H";
    String TRANSACTIONS_CELL_RANGE_WITHOUT_HEADING = "B4:H";
    String TRANSACTION_START_COL = "B";
    String TRANSACTION_END_COL = "H";
    int TRANSACTION_ROW_COLUMN_COUNT = 7;

}

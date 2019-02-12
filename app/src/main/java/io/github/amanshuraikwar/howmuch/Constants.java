package io.github.amanshuraikwar.howmuch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constants {

    String DEFAULT_ERROR_MESSAGE = "Something went wrong!";

    String DEFAULT_USER_NAME = "HowMuch User";
    String DEFAULT_USER_EMAIL = "user@example.com";

    String METADATA_SHEET_TITLE = "Metadata";

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

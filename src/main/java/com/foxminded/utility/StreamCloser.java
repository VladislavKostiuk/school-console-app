package com.foxminded.utility;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class StreamCloser {
    private StreamCloser() {}

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

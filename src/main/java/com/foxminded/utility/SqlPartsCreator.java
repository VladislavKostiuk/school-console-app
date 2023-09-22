package com.foxminded.utility;

public final class SqlPartsCreator {
    private SqlPartsCreator() {}

    public static String createInPart(int listSize) {
        StringBuilder sql = new StringBuilder("IN (");
        for (int i = 0; i < listSize; i++) {
            sql.append("?");

            if (i != listSize - 1) {
                sql.append(", ");
            } else {
                sql.append(")");
            }
        }

        return sql.toString();
    }
}

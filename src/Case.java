package src;

import java.util.HashMap;
import java.util.Map;

public enum Case {
    EMPTY(0),
    BLACK(2),
    RED(4);

    private final int value;

    Case(final int newValue) {
        value = newValue;
    }

    public static Case fromInteger(int x) {
        switch (x) {
            case 0:
                return EMPTY;
            case 2:
                return BLACK;
            case 4:
                return RED;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public static final Map<String, int[]> MapCase = new HashMap<>();

    public static void InitCaseNameToArray() {
        char[] columns = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' };
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String key = columns[j] + String.valueOf(8 - i);
                int[] indexPair = { i, j };
                MapCase.put(key, indexPair);
            }
        }
    }

    public static int[] getIndexesByCaseName(String key) {
        return MapCase.get(key);
    }

    public String getCaseNameByIndexes(int[] indexPair) {
        for (Map.Entry<String, int[]> entry : MapCase.entrySet()) {
            if (entry.getValue()[0] == indexPair[0] && entry.getValue()[1] == indexPair[1]) {
                return entry.getKey();
            }
        }
        return null;
    }
     public static String getCaseNameByIndexes(int col, int row) {
        for (Map.Entry<String, int[]> entry : MapCase.entrySet()) {
            if (entry.getValue()[0] == col && entry.getValue()[1] == row) {
                return entry.getKey();
            }
        }
        return null;
    }
}

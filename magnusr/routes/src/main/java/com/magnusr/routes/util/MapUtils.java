package com.magnusr.routes.util;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * MapUtils
 *
 * A class containing static helper / utility methods.
 */
public class MapUtils {

    /**
     * Removes duplicates from a list value in a map
     */
    public static <T> void removeDuplicatesFromListValue(Map<T, List<T>> map) {
        for (var entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().distinct().collect(toList()));
        }
    }
}

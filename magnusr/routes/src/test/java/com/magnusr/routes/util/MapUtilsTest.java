package com.magnusr.routes.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.magnusr.routes.util.MapUtils.removeDuplicatesFromListValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapUtilsTest {

    @Test
    void testRemoveDuplicatesFromListValueInMap() {
        var map = new HashMap<String, List<String>>();

        map.put("List1", new ArrayList<>());
        map.put("List2", new ArrayList<>());
        map.get("List1").add("String 1");
        map.get("List1").add("String 2");
        map.get("List1").add("String 2");
        map.get("List2").add("String 1");
        map.get("List2").add("String 2");

        removeDuplicatesFromListValue(map);

        assertEquals(2, map.get("List1").size());
        assertEquals(2, map.get("List2").size());
    }

}

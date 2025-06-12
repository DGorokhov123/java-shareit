package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemsEndpointTest extends HttpAbstractTest {

    @Test
    void badRequests() {
        Long userId = makeUserAndReturnId("{\"email\":\"bad.requests@test.ru\",\"name\":\"Bad Requests\"}");

        assertTrue(checkPostWithHeader("/items", "", 400, "Illegal Argument", userId));        // empty json
        assertTrue(checkPostWithHeader("/items", "hrtfhyrth", 400, "Illegal Argument", userId));        // random string
        assertTrue(checkPostWithHeader("/items", "{}", 400, "Validation", userId));           // empty object
        assertTrue(checkPostWithHeader("/items", "{\"king\":\"sauron\"}", 400, "Validation", userId));    // other object

        Long itemId = makeItemAndReturnId("{\"name\":\"bad.tool\",\"description\":\"bad tool\",\"available\":true}", userId);

        assertTrue(checkPatchWithHeader("/items/" + itemId, "", 400, "Illegal Argument", userId));        // empty string
        assertTrue(checkPatchWithHeader("/items/" + itemId, "hrtfhyrth", 400, "Illegal Argument", userId));    // random string
        assertTrue(checkPatchWithHeader("/items/" + itemId, "{}", 400, "Validation", userId));           // empty json
        assertTrue(checkPatchWithHeader("/items/" + itemId, "{\"king\":\"sauron\"}", 400, "Validation", userId));    // other object

        assertTrue(checkGet("/items/f", 400, "Illegal Argument"));
        assertTrue(checkGet("/items/-1", 400, "Illegal Argument"));
        assertTrue(checkGet("/items/0", 400, "Illegal Argument"));

        assertTrue(checkDeleteWithHeader("/items/f", 400, "Illegal Argument", userId));
        assertTrue(checkDeleteWithHeader("/items/-1", 400, "Illegal Argument", userId));
        assertTrue(checkDeleteWithHeader("/items/0", 400, "Illegal Argument", userId));
    }

    @Test
    void withoutHeaderRequests() {
        Long userId = makeUserAndReturnId("{\"email\":\"without.header@test.ru\",\"name\":\"without header\"}");
        Long itemId = makeItemAndReturnId("{\"name\":\"without.header.tool\",\"description\":\"without header tool\",\"available\":true}", userId);

        assertTrue(checkPost("/items", "{" + """
                    "name" : "without.header.tool.2",
                    "description" : "without header tool 2",
                    "available" : true
                }
                """, 400, "Header"));

        assertTrue(checkPatch("/items/" + itemId, "{" + """
                    "name" : "without.header.tool.3"
                }
                """, 400, "Header"));

        assertTrue(checkDelete("/items/" + itemId, 400, "Header"));

        String[] searches = {"Missing", "Header"};
        assertTrue(checkGetAll("/items", 400, searches));
    }

    @Test
    void notFoundRequests() {
        Long userId = makeUserAndReturnId("{\"email\":\"not.found@test.ru\",\"name\":\"not found\"}");
        Long itemId = makeItemAndReturnId("{\"name\":\"not.found.tool\",\"description\":\"not found tool\",\"available\":true}", userId);

        // Item not found
        assertTrue(checkGet("/items/" + (itemId + 1), 404, "not found"));

        assertTrue(checkDeleteWithHeader("/items/" + (itemId + 1), 404, "not found", userId));

        assertTrue(checkPatchWithHeader("/items/" + (itemId + 1), "{" + """
                    "name" : "not.found.tool.2"
                }
                """, 404, "not found", userId));

        // User not found
        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "not.found.tool.3",
                    "description" : "no matter",
                    "available" : true
                }
                """, 404, "not found", userId + 1));

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "name" : "not.found.tool.4"
                }
                """, 404, "not found", userId + 1));

        assertTrue(checkDeleteWithHeader("/items/" + itemId, 404, "not found", userId + 1));
    }

    @Test
    void nameValidation() {
        Long userId = makeUserAndReturnId("{\"email\":\"name.val@test.ru\",\"name\":\"name val\"}");

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : " ",
                    "description" : "no matter",
                    "available" : true
                }
                """, 400, "Validation", userId));                     // blank name

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "description" : "no matter",
                    "available" : true
                }
                """, 400, "Validation", userId));                     // without name

        Long itemId = makeItemAndReturnId("{\"name\":\"name.val.tool\",\"description\":\"name val tool\",\"available\":true}", userId);

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "name" : " "
                }
                """, 400, "Validation", userId));                     // blank name
    }

    @Test
    void descriptionValidation() {
        Long userId = makeUserAndReturnId("{\"email\":\"desc.val@test.ru\",\"name\":\"desc val\"}");

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "desc.val.tool",
                    "description" : " ",
                    "available" : true
                }
                """, 400, "Validation", userId));                     // blank description

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "desc.val.tool",
                    "available" : true
                }
                """, 400, "Validation", userId));                     // without description

        Long itemId = makeItemAndReturnId("{\"name\":\"desc.val.tool\",\"description\":\"desc val tool\",\"available\":true}", userId);

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "description" : " "
                }
                """, 400, "Validation", userId));                     // blank description
    }

    @Test
    void availableValidation() {
        Long userId = makeUserAndReturnId("{\"email\":\"available.val@test.ru\",\"name\":\"available val\"}");

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "available.val.tool",
                    "description" : "available val tool",
                    "available" : "f"
                }
                """, 400, "Illegal", userId));                     // wrong available

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "available.val.tool",
                    "description" : "available val tool"
                }
                """, 400, "Validation", userId));                     // without available

        Long itemId = makeItemAndReturnId("{\"name\":\"available.val.tool\",\"description\":\"available val tool\",\"available\":true}", userId);

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "available" : "f"
                }
                """, 400, "Illegal", userId));                     // wrong available
    }

    @Test
    void crudGoodScenario() {
        Long userId = makeUserAndReturnId("{\"email\":\"good.crud@test.ru\",\"name\":\"good crud\"}");

        // POST
        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "good.crud.tool.1",
                    "description" : "good crud tool 1",
                    "available" : true
                }
                """, 200, "good.crud.tool.1", userId));

        assertTrue(checkPostWithHeader("/items", "{" + """
                    "name" : "good.crud.tool.2",
                    "description" : "good crud tool 2",
                    "available" : true
                }
                """, 200, "good.crud.tool.2", userId));

        // PATCH
        Long itemId = makeItemAndReturnId("{\"name\":\"good.crud.tool.3\",\"description\":\"good crud tool 3\",\"available\":true}", userId);

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "name" : "lopata"
                }
                """, 200, "lopata", userId));

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "description" : "vedro"
                }
                """, 200, "vedro", userId));

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "available" : false
                }
                """, 200, "false", userId));

        assertTrue(checkPatchWithHeader("/items/" + itemId, "{" + """
                    "name" : "good.crud.tool.4",
                    "description" : "good crud tool 4",
                    "available" : true
                }
                """, 200, "good.crud.tool.4", userId));

        // GET BY ID
        assertTrue(checkGet("/items/" + itemId, 200, "good.crud.tool.4"));

        // GET BY USER ID
        String[] searches1 = {"good.crud.tool.1", "good.crud.tool.2", "good.crud.tool.4"};
        assertTrue(checkGetAllWithHeader("/items", 200, searches1, userId));

        Long userId2 = makeUserAndReturnId("{\"email\":\"good.crud2@test.ru\",\"name\":\"good crud 2\"}");

        String[] searches2 = {"[]"};
        assertTrue(checkGetAllWithHeader("/items", 200, searches2, userId2));

        // SEARCH BY TEXT
        String[] searches5 = {"good.crud.tool.1", "good.crud.tool.2", "good.crud.tool.4"};
        assertTrue(checkGetAll("/items/search?text=cRuD", 200, searches5));

        String[] searches6 = {"[]"};
        assertTrue(checkGetAll("/items/search?text=dabudidabuda", 200, searches6));

        String[] searches7 = {"[]"};
        assertTrue(checkGetAll("/items/search", 200, searches7));

        // DELETE
        assertTrue(checkDeleteWithHeader("/items/" + itemId, 200, null, userId));

    }

}

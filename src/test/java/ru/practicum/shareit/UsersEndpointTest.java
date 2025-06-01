package ru.practicum.shareit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersEndpointTest extends HttpAbstractTest {

    @Test
    void badRequests() {
        assertTrue(checkPost("/users", "", 400, "Illegal Argument"));        // empty json
        assertTrue(checkPost("/users", "hrtfhyrth", 400, "Illegal Argument"));        // random string
        assertTrue(checkPost("/users", "{}", 400, "Validation"));           // empty object
        assertTrue(checkPost("/users", "{\"king\":\"sauron\"}", 400, "Validation"));    // other object

        Long userId = makeUserAndReturnId("{\"email\":\"badJsons@test.ru\",\"name\":\"mrBadJsons\"}");

        assertTrue(checkPatch("/users/" + userId, "", 400, "Illegal Argument"));        // empty string
        assertTrue(checkPatch("/users/" + userId, "hrtfhyrth", 400, "Illegal Argument"));    // random string
        assertTrue(checkPatch("/users/" + userId, "{}", 400, "Validation"));           // empty json
        assertTrue(checkPatch("/users/" + userId, "{\"king\":\"sauron\"}", 400, "Validation"));    // other object

        assertTrue(checkGet("/users/f", 400, "Illegal Argument"));
        assertTrue(checkGet("/users/-1", 400, "Illegal Argument"));
        assertTrue(checkGet("/users/0", 400, "Illegal Argument"));

        assertTrue(checkDelete("/users/f", 400, "Illegal Argument"));
        assertTrue(checkDelete("/users/-1", 400, "Illegal Argument"));
        assertTrue(checkDelete("/users/0", 400, "Illegal Argument"));
    }

    @Test
    void notFoundRequests() {
        Long userId = makeUserAndReturnId("{\"email\":\"not.found1@test.ru\",\"name\":\"no matter\"}") + 1;

        assertTrue(checkGet("/users/" + userId, 404, "not found"));

        assertTrue(checkDelete("/users/" + userId, 404, "not found"));

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "not.found2@test.ru"
                }
                """, 404, "not found"));
    }

    @Test
    void emailValidation() {
        assertTrue(checkPost("/users", "{" + """
                    "email" : "em.val.test.ru",
                    "name" : "no matter"
                }
                """, 400, "Validation"));                          // bad email

        assertTrue(checkPost("/users", "{" + """
                    "email" : "",
                    "name" : "no matter"
                }
                """, 400, "Validation"));                // empty email

        assertTrue(checkPost("/users", "{" + """
                    "name" : "no matter"
                }
                """, 400, "Validation"));              // without email

        makeUserAndReturnId("{\"email\":\"em.val1@test.ru\",\"name\":\"no matter 1\"}");

        assertTrue(checkPost("/users", "{" + """
                    "email" : "em.val1@test.ru",
                    "name" : "no matter"
                }
                """, 409, "Conflict"));                         // duplicate email

        Long userId = makeUserAndReturnId("{\"email\":\"emailvalidation2@test.ru\",\"name\":\"no matter 2\"}");

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "em.val.test.ru"
                }
                """, 400, "Validation"));                          // bad email

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : ""
                }
                """, 400, "Validation"));                        // empty email

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "em.val1@test.ru"
                }
                """, 409, "Conflict"));                         // duplicate email

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "emailvalidation2@test.ru"
                }
                """, 200, "emailvalidation2@test.ru"));               // the same email
    }

    @Test
    void nameValidation() {
        assertTrue(checkPost("/users", "{" + """
                    "email" : "name.val@test.ru",
                    "name" : " "
                }
                """, 400, "Validation"));                     // blank name

        assertTrue(checkPost("/users", "{" + """
                    "email" : "name.val@test.ru"
                }
                """, 400, "Validation"));              // without name

        Long userId = makeUserAndReturnId("{\"email\":\"name.val1@test.ru\",\"name\":\"no matter 1\"}");

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "name" : " "
                }
                """, 400, "Validation"));                     // blank name
    }

    @Test
    void crudGoodScenario() {
        // POST
        assertTrue(checkPost("/users", "{" + """
                    "email" : "good.user1@test.ru",
                    "name" : "ivan durak"
                }
                """, 200, "ivan durak"));

        assertTrue(checkPost("/users", "{" + """
                    "email" : "good.user2@test.ru",
                    "name" : "sofochka"
                }
                """, 200, "sofochka"));

        // PATCH
        Long userId = makeUserAndReturnId("{\"email\":\"good.user3@test.ru\",\"name\":\"olezhe\"}");

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "good.user4@test.ru"
                }
                """, 200, "good.user4@test.ru"));

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "name" : "NewName123"
                }
                """, 200, "NewName123"));

        assertTrue(checkPatch("/users/" + userId, "{" + """
                    "email" : "good.user5@test.ru",
                    "name" : "Onotole"
                }
                """, 200, "good.user5@test.ru"));

        // GET BY ID
        assertTrue(checkGet("/users/" + userId, 200, "Onotole"));

        // GET ALL
        String[] searches = {"ivan durak", "sofochka", "Onotole"};
        assertTrue(checkGetAll("/users", 200, searches));

        // DELETE
        assertTrue(checkDelete("/users/" + userId, 200, null));
    }

}

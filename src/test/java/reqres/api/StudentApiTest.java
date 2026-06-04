    package reqres.api;
    import io.restassured.builder.RequestSpecBuilder;
    import io.restassured.filter.log.LogDetail;
    import io.restassured.http.ContentType;
    import io.restassured.response.Response;
    import io.restassured.specification.RequestSpecification;
    import org.json.JSONObject;
    import org.testng.annotations.Test;
    import reqres.api.model.JsonUser;

    import static java.net.HttpURLConnection.*;

    import static io.restassured.RestAssured.given;
    import static org.hamcrest.Matchers.*;
    import static org.testng.Assert.assertNotNull;
    import static org.testng.Assert.assertTrue;

    public class StudentApiTest {

        private final static String BASE_URL = "https://reqres.in/api/";

        private final RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("x-api-key", "free_user_3EgTdnilLS5zA4VuP8XHB0mOIBk")
                .log(LogDetail.ALL)
                .build();

        @Test
        public void testGetAvailablePage_shouldReturnPageOfUsers() {
            given()
                    .spec(requestSpec)
                    .get("users?page=1")
                    .then()
                    .statusCode(HTTP_OK)
                    .body("page", equalTo(1))
                    .body("per_page", equalTo(6))
                    .body("data.id[0]", equalTo(1))
                    .body("data.id[5]", equalTo(6))
                    .body("data.last_name", hasItems(
                            "Weaver",
                            "Holt",
                            "Wong",
                            "Ramos"
                    ))
                    .body("data.email", hasItems(
                            "george.bluth@reqres.in",
                            "charles.morris@reqres.in",
                            "tracey.ramos@reqres.in"
                    ));
        }

        @Test
        public void testGetUnavailablePage_shouldReturnEmpty() {
            given().spec(requestSpec)
                    .get("/users?page=999")
                    .then()
                    .statusCode(HTTP_OK)
                    .body("data", empty());
        }

        @Test
        public void testPostCreateUser_shouldReturnSomeUserCreatingInfo() {
            JSONObject userData =
                    new JsonUser("Bill", "Developer")
                            .buildJsonObject();

            Response response = given()
                    .spec(requestSpec)
                    .body(userData)
                    .when()
                    .post("users")
                    .then()
                    .statusCode(HTTP_CREATED).extract().response();

            JSONObject createdUser = new JSONObject(response.getBody().print());

            assertNotNull(createdUser.get("id"));
            assertNotNull(createdUser.get("createdAt"));
            assertTrue(Integer.parseInt(createdUser.get("id").toString()) > 0);
        }

        @Test
        public void testPutUpdateUser_shouldReturnSomeUserUpdatingInfo() {
            JSONObject updatedUserData =
                    new JsonUser("Bill", "Senior developer")
                            .buildJsonObject();

            Response response = given()
                    .spec(requestSpec)
                    .body(updatedUserData)
                    .when()
                    .put("users/2")
                    .then()
                    .statusCode(HTTP_OK).extract().response();

            JSONObject updatedUser = new JSONObject(response.getBody().print());

            assertNotNull(updatedUser.get("updatedAt"));
        }

        @Test
        public void testDelete_shouldReturnNoContent() {
            given()
                    .spec(requestSpec)
                    .when()
                    .delete("users/2")
                    .then()
                    .statusCode(HTTP_NO_CONTENT);
        }
        @Test
        public void testGetSingleUser_shouldReturnUserWithId3() {
            // Validate that a single user with id 3 can be fetched successfully
            given().spec(requestSpec)
                    .when()
                    .get("users/3")
                    .then()
                    .statusCode(HTTP_OK)
                    .body("data.id", equalTo(3))
                    .body("data.first_name", notNullValue());
        }

        @Test
        public void testGetSingleUser_shouldReturn404ForInvalidUser() {
            // Validate that invalid user id returns 404 not found
            given().spec(requestSpec)
                    .when()
                    .get("users/9999")
                    .then()
                    .statusCode(HTTP_NOT_FOUND);
        }
    }

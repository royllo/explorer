package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.UserByUserIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.UserByUserIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.UserByUsernameGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.UserByUsernameProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("UserDataFetcher tests")
public class UserDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("getUserByUserId()")
    public void getUserByUserId() {
        User user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        UserByUserIdGraphQLQuery.newRequest().userId("11111111-1111-1111-1111-111111111111").build(),
                        new UserByUserIdProjectionRoot<>().userId().username())
                        .serialize(),
                "data." + DgsConstants.QUERY.UserByUserId,
                new TypeRef<>() {
                });

        // Testing the results.
        assertNotNull(user);
        assertEquals("11111111-1111-1111-1111-111111111111", user.getUserId());
        assertEquals("straumat", user.getUsername());
    }

    @Test
    @DisplayName("getUserByUsername()")
    public void getUserByUsername() {
        User user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        UserByUsernameGraphQLQuery.newRequest().username("straumat").build(),
                        new UserByUsernameProjectionRoot<>().userId().username())
                        .serialize(),
                "data." + DgsConstants.QUERY.UserByUsername,
                new TypeRef<>() {
                });

        // Testing the results.
        assertNotNull(user);
        assertEquals("11111111-1111-1111-1111-111111111111", user.getUserId());
        assertEquals("straumat", user.getUsername());
    }

}

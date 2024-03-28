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
import org.royllo.explorer.api.graphql.generated.types.User;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@DisplayName("UserDataFetcher tests")
public class UserDataFetcherTest extends BaseTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("userByUserId()")
    public void userByUserId() {
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        UserByUserIdGraphQLQuery.newRequest().userId(STRAUMAT_USER_USER_ID).build(),
                        new UserByUserIdProjectionRoot<>().userId().username())
                        .serialize(),
                "data." + DgsConstants.QUERY.UserByUserId,
                new TypeRef<User>() {
                }))
                .isNotNull()
                .satisfies(user -> {
                    assertEquals(STRAUMAT_USER_USER_ID, user.getUserId());
                    assertEquals(STRAUMAT_USER_USERNAME, user.getUsername());
                });
    }

    @Test
    @DisplayName("userByUsername()")
    public void userByUsername() {
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        UserByUsernameGraphQLQuery.newRequest().username(STRAUMAT_USER_USERNAME).build(),
                        new UserByUserIdProjectionRoot<>().userId().username())
                        .serialize(),
                "data." + DgsConstants.QUERY.UserByUsername,
                new TypeRef<User>() {
                }))
                .isNotNull()
                .satisfies(user -> {
                    assertEquals(STRAUMAT_USER_USER_ID, user.getUserId());
                    assertEquals(STRAUMAT_USER_USERNAME, user.getUsername());
                });
    }

}

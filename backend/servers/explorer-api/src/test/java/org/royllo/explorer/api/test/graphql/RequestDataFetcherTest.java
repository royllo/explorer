package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.CreateAddProofRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.CreateAddProofRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.CreateAddUniverseServerRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.CreateAddUniverseServerRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.RequestByRequestIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.RequestByRequestIdProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AddProofRequest;
import org.royllo.explorer.api.graphql.generated.types.AddProofRequestInputs;
import org.royllo.explorer.api.graphql.generated.types.AddUniverseServerRequest;
import org.royllo.explorer.api.graphql.generated.types.AddUniverseServerRequestInputs;
import org.royllo.explorer.api.graphql.generated.types.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_ID;
import static org.royllo.explorer.core.util.constants.AnonymousUserConstants.ANONYMOUS_USER_USERNAME;
import static org.royllo.explorer.core.util.enums.RequestStatus.OPENED;

@SpringBootTest
@DirtiesContext
@DisplayName("RequestDataFetcher tests")
public class RequestDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("requestByRequestId()")
    public void requestByRequestId() {
        // Getting a specific request.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        RequestByRequestIdGraphQLQuery.newRequest().requestId("91425ba6-8b16-46a8-baa6-request_p_03").build(),
                        new RequestByRequestIdProjectionRoot<>()
                                .requestId()
                                .creator().userId().username().parent()
                                .status().getParent()
                                .errorMessage()
                                .onAddProofRequest().proof().parent()
                                .onAddUniverseServerRequest().serverAddress()
                ).serialize(),
                "data." + DgsConstants.QUERY.RequestByRequestId,
                new TypeRef<Request>() {
                }))
                .isNotNull()
                .satisfies(request -> {
                    AddProofRequest addAssetRequest = (AddProofRequest) request;
                    assertEquals("91425ba6-8b16-46a8-baa6-request_p_03", addAssetRequest.getRequestId());
                    assertEquals(ANONYMOUS_USER_ID, addAssetRequest.getCreator().getUserId());
                    assertEquals(ANONYMOUS_USER_USERNAME, addAssetRequest.getCreator().getUsername());
                    assertEquals(OPENED.toString(), addAssetRequest.getStatus().toString());
                    assertNull(addAssetRequest.getErrorMessage());
                    assertEquals("P4", addAssetRequest.getProof());
                });
    }

    @Test
    @DisplayName("createAddProofRequest()")
    public void createAddProofRequest() {
        // Creating a new request to add proof.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        CreateAddProofRequestGraphQLQuery.newRequest()
                                .input(AddProofRequestInputs.newBuilder()
                                        .proof("6")
                                        .build())
                                .build(),
                        new CreateAddProofRequestProjectionRoot<>()
                                .requestId()
                                .creator().userId().username().parent()
                                .status().getParent()
                                .errorMessage().proof()
                ).serialize(),
                "data." + DgsConstants.MUTATION.CreateAddProofRequest,
                new TypeRef<AddProofRequest>() {
                }))
                .isNotNull()
                .satisfies(addProofRequest -> {
                    assertEquals(ANONYMOUS_USER_ID, addProofRequest.getCreator().getUserId());
                    assertEquals(ANONYMOUS_USER_USERNAME, addProofRequest.getCreator().getUsername());
                    assertEquals(OPENED.toString(), addProofRequest.getStatus().toString());
                    assertNull(addProofRequest.getErrorMessage());
                    assertEquals("6", addProofRequest.getProof());
                });
    }

    @Test
    @DisplayName("createAddUniverseServerRequest()")
    public void createAddUniverseServerRequest() {
        // Creating a new request to add a universe server.
        assertThat(dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                new GraphQLQueryRequest(
                        CreateAddUniverseServerRequestGraphQLQuery.newRequest()
                                .input(AddUniverseServerRequestInputs.newBuilder()
                                        .serverAddress("1.1.1.1:8080")
                                        .build())
                                .build(),
                        new CreateAddUniverseServerRequestProjectionRoot<>()
                                .requestId()
                                .creator().userId().username().parent()
                                .status().getParent()
                                .errorMessage()
                                .serverAddress()
                ).serialize(),
                "data." + DgsConstants.MUTATION.CreateAddUniverseServerRequest,
                new TypeRef<AddUniverseServerRequest>() {
                }))
                .isNotNull()
                .satisfies(addUniverseServerRequest -> {
                    assertNotNull(addUniverseServerRequest.getRequestId());
                    assertEquals(ANONYMOUS_USER_ID, addUniverseServerRequest.getCreator().getUserId());
                    assertEquals(ANONYMOUS_USER_USERNAME, addUniverseServerRequest.getCreator().getUsername());
                    assertEquals(OPENED.toString(), addUniverseServerRequest.getStatus().toString());
                    assertNull(addUniverseServerRequest.getErrorMessage());
                    assertEquals("1.1.1.1:8080", addUniverseServerRequest.getServerAddress());
                });
    }

}

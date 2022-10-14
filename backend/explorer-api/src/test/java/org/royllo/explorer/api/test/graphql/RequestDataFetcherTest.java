package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AddAssetMetaDataRequest;
import org.royllo.explorer.api.graphql.generated.types.AddAssetRequest;
import org.royllo.explorer.api.graphql.generated.types.Request;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DisplayName("RequestDataFetcher tests")
public class RequestDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("openedRequests()")
    public void openedRequests() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                OpenedRequestsGraphQLQuery.newRequest().build(),
                new OpenedRequestsProjectionRoot()
                        .id()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage()
                        .onAddAssetRequest().genesisBootstrapInformation().proof().getParent()
                        .onAddAssetMetaDataRequest().assetId().metaData());

        List<Request> requests = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.OpenedRequests + "[*]",
                new TypeRef<>() {
                });
        assertEquals(3, requests.size());

        // Request 1.
        final AddAssetRequest request1 = (AddAssetRequest) requests.get(0);
        assertEquals("1", request1.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), request1.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request1.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), request1.getStatus().toString());
        assertNull(request1.getErrorMessage());
        assertEquals("GI1", request1.getGenesisBootstrapInformation());
        assertEquals("P1", request1.getProof());

        // Request 2.
        final AddAssetMetaDataRequest request2 = (AddAssetMetaDataRequest) requests.get(1);
        assertEquals("3", request2.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), request2.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request2.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), request2.getStatus().toString());
        assertNull(request2.getErrorMessage());
        assertEquals("AI2", request2.getAssetId());
        assertEquals("MD2", request2.getMetaData());

        // Request 3.
        final AddAssetRequest request3 = (AddAssetRequest) requests.get(2);
        assertEquals("4", request3.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), request3.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request3.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), request3.getStatus().toString());
        assertNull(request3.getErrorMessage());
        assertEquals("GI4", request3.getGenesisBootstrapInformation());
        assertEquals("P4", request3.getProof());
    }

}

package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.AddAssetMetaDataRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AddAssetMetaDataRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.AddProofRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AddProofRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.RequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.RequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AddAssetMetaDataRequest;
import org.royllo.explorer.api.graphql.generated.types.AddProofRequest;
import org.royllo.explorer.api.graphql.generated.types.AddProofRequestInputs;
import org.royllo.explorer.api.graphql.generated.types.Request;
import org.royllo.explorer.api.graphql.generated.types.addAssetMetaDataRequestInputs;
import org.royllo.explorer.core.util.constants.UserConstants;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("RequestDataFetcher tests")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
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
                        .onAddProofRequest().rawProof().getParent()
                        .onAddAssetMetaDataRequest().assetId().metaData());

        List<Request> requests = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.OpenedRequests + "[*]",
                new TypeRef<>() {
                });
        assertEquals(3, requests.size());

        // Request 1.
        final AddProofRequest request1 = (AddProofRequest) requests.get(0);
        assertEquals("1", request1.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), request1.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request1.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), request1.getStatus().toString());
        assertNull(request1.getErrorMessage());
        assertEquals("P1", request1.getRawProof());

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
        final AddProofRequest request3 = (AddProofRequest) requests.get(2);
        assertEquals("4", request3.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), request3.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, request3.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), request3.getStatus().toString());
        assertNull(request3.getErrorMessage());
        assertEquals("P4", request3.getRawProof());
    }

    @Test
    @DisplayName("request()")
    public void request() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                RequestGraphQLQuery.newRequest().id("4").build(),
                new RequestProjectionRoot()
                        .id()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage()
                        .onAddProofRequest().rawProof().parent()
                        .onAddAssetMetaDataRequest().assetId().metaData());

        Request request = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.Request,
                new TypeRef<>() {
                });

        AddProofRequest addAssetRequest = (AddProofRequest) request;
        assertEquals("4", addAssetRequest.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), addAssetRequest.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, addAssetRequest.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), addAssetRequest.getStatus().toString());
        assertNull(addAssetRequest.getErrorMessage());
        assertEquals("P4", addAssetRequest.getRawProof());
    }

    @Test
    @DisplayName("addProofRequest()")
    public void addProofRequest() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AddProofRequestGraphQLQuery.newRequest()
                        .input(AddProofRequestInputs.newBuilder()
                                .rawProof("6")
                                .build())
                        .build(),
                new AddProofRequestProjectionRoot()
                        .id()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage().rawProof());

        AddProofRequest requestCreated = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.MUTATION.AddProofRequest,
                new TypeRef<>() {
                });

        assertEquals("5", requestCreated.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), requestCreated.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, requestCreated.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), requestCreated.getStatus().toString());
        assertNull(requestCreated.getErrorMessage());
        assertEquals("6", requestCreated.getRawProof());
    }

    @Test
    @DisplayName("addAssetMetaDataRequest()")
    public void addAssetMetaDataRequest() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AddAssetMetaDataRequestGraphQLQuery.newRequest()
                        .input(addAssetMetaDataRequestInputs.newBuilder()
                                .assetId("AssetID1")
                                .metaData("MetaData01")
                                .build())
                        .build(),
                new AddAssetMetaDataRequestProjectionRoot()
                        .id()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage()
                        .assetId()
                        .metaData());

        AddAssetMetaDataRequest requestCreated = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.MUTATION.AddAssetMetaDataRequest,
                new TypeRef<>() {
                });

        assertEquals("5", requestCreated.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), requestCreated.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, requestCreated.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), requestCreated.getStatus().toString());
        assertNull(requestCreated.getErrorMessage());
        assertEquals("AssetID1", requestCreated.getAssetId());
        assertEquals("MetaData01", requestCreated.getMetaData());
    }

}

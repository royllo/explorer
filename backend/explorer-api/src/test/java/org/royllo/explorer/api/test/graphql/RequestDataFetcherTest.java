package org.royllo.explorer.api.test.graphql;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.api.graphql.generated.DgsConstants;
import org.royllo.explorer.api.graphql.generated.client.AddAssetMetaDataRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AddAssetMetaDataRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.AddAssetRequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.AddAssetRequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.OpenedRequestsProjectionRoot;
import org.royllo.explorer.api.graphql.generated.client.RequestGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.RequestProjectionRoot;
import org.royllo.explorer.api.graphql.generated.types.AddAssetMetaDataRequest;
import org.royllo.explorer.api.graphql.generated.types.AddAssetRequest;
import org.royllo.explorer.api.graphql.generated.types.AddAssetRequestInputs;
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
                        .onAddAssetRequest().genesisPoint().name().metaData().assetId().outputIndex().proof().getParent()
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
        assertEquals("GP1", request1.getGenesisPoint());
        assertEquals("NAME1", request1.getName());
        assertEquals("ME1", request1.getMetaData());
        assertEquals("AI1", request1.getAssetId());
        assertEquals(1, request1.getOutputIndex());
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
        assertEquals("GP4", request3.getGenesisPoint());
        assertEquals("NAME4", request3.getName());
        assertEquals("ME4", request3.getMetaData());
        assertEquals("AI4", request3.getAssetId());
        assertEquals(4, request3.getOutputIndex());
        assertEquals("P4", request3.getProof());
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
                        .onAddAssetRequest().genesisPoint().name().metaData().assetId().outputIndex().proof().getParent()
                        .onAddAssetMetaDataRequest().assetId().metaData());

        Request request = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.Request,
                new TypeRef<>() {
                });

        AddAssetRequest addAssetRequest = (AddAssetRequest) request;
        assertEquals("4", addAssetRequest.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), addAssetRequest.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, addAssetRequest.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), addAssetRequest.getStatus().toString());
        assertNull(addAssetRequest.getErrorMessage());
        assertEquals("GP4", addAssetRequest.getGenesisPoint());
        assertEquals("NAME4", addAssetRequest.getName());
        assertEquals("ME4", addAssetRequest.getMetaData());
        assertEquals("AI4", addAssetRequest.getAssetId());
        assertEquals(4, addAssetRequest.getOutputIndex());
        assertEquals("P4", addAssetRequest.getProof());
    }

    @Test
    @DisplayName("addAssetRequest()")
    public void addAssetRequest() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                AddAssetRequestGraphQLQuery.newRequest()
                        .input(AddAssetRequestInputs.newBuilder()
                                .genesisPoint("1")
                                .name("2")
                                .metaData("3")
                                .assetId("4")
                                .outputIndex(5)
                                .proof("proof01")
                                .proof("6")
                                .build())
                        .build(),
                new AddAssetRequestProjectionRoot()
                        .id()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage()
                        .genesisPoint().name().metaData().assetId().outputIndex().proof());

        AddAssetRequest requestCreated = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.MUTATION.AddAssetRequest,
                new TypeRef<>() {
                });

        assertEquals("5", requestCreated.getId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), requestCreated.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, requestCreated.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), requestCreated.getStatus().toString());
        assertNull(requestCreated.getErrorMessage());
        assertEquals("1", requestCreated.getGenesisPoint());
        assertEquals("2", requestCreated.getName());
        assertEquals("3", requestCreated.getMetaData());
        assertEquals("4", requestCreated.getAssetId());
        assertEquals(5, requestCreated.getOutputIndex());
        assertEquals("6", requestCreated.getProof());
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

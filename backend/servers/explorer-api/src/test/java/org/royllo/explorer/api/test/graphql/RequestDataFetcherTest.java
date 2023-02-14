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
import org.royllo.explorer.api.graphql.generated.client.RequestByRequestIdGraphQLQuery;
import org.royllo.explorer.api.graphql.generated.client.RequestByRequestIdProjectionRoot;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DisplayName("RequestDataFetcher tests")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class RequestDataFetcherTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @DisplayName("requestByRequestId()")
    public void requestByRequestId() {
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                RequestByRequestIdGraphQLQuery.newRequest().requestId("91425ba6-8b16-46a8-baa6-request_p_03").build(),
                new RequestByRequestIdProjectionRoot()
                        .requestId()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage()
                        .onAddProofRequest().rawProof().parent()
                        .onAddAssetMetaDataRequest().assetId().metaData());

        Request request = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.QUERY.RequestByRequestId,
                new TypeRef<>() {
                });

        AddProofRequest addAssetRequest = (AddProofRequest) request;
        assertEquals("91425ba6-8b16-46a8-baa6-request_p_03", addAssetRequest.getRequestId());
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
                        .requestId()
                        .creator().id().username().parent()
                        .status().getParent()
                        .errorMessage().rawProof());

        AddProofRequest requestCreated = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data." + DgsConstants.MUTATION.AddProofRequest,
                new TypeRef<>() {
                });

        assertNotNull(requestCreated.getRequestId());
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
                        .requestId()
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

        assertNotNull(requestCreated.getRequestId());
        assertEquals(UserConstants.ANONYMOUS_USER_ID.toString(), requestCreated.getCreator().getId());
        assertEquals(UserConstants.ANONYMOUS_USER_USERNAME, requestCreated.getCreator().getUsername());
        assertEquals(RequestStatus.OPENED.toString(), requestCreated.getStatus().toString());
        assertNull(requestCreated.getErrorMessage());
        assertEquals("AssetID1", requestCreated.getAssetId());
        assertEquals("MetaData01", requestCreated.getMetaData());
    }

}

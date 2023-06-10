package org.royllo.explorer.core.provider.tapd;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.BaseMempoolService;
import org.royllo.explorer.core.util.parameters.TAPDParameters;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

/**
 * TAPD service implementation.
 */
@Service
@RequiredArgsConstructor
public class TapdServiceImplementation extends BaseMempoolService implements TapdService {

    /** TAPD parameters. */
    private final TAPDParameters tapdParameters;

    /** SSL Context. */
    private SslContext sslContext;

    /**
     * Getter sslContext.
     *
     * @return sslContext
     */
    public final SslContext getSslContext() {
        // We build a ssl Context where valid SSL is not required.
        if (sslContext == null) {
            try {
                sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
        }
        return sslContext;
    }

    @Override
    public final Mono<DecodedProofResponse> decode(final String rawProof, final long proofIndex) {
        logger.info("Calling decode for proof from tapd n°{} with raw proof {}", proofIndex, rawProof);
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(tapdParameters.getApi().getBaseUrl())
                .build()
                .post()
                .uri("proofs/decode")
                .header("Grpc-Metadata-macaroon", tapdParameters.getApi().getMacaroon())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ProofRequest.builder()
                        .rawProof(rawProof)
                        .proofIndex(proofIndex)
                        .build()
                ))
                .exchangeToFlux(response -> response.bodyToFlux(DecodedProofResponse.class))
                .next();
    }

    @Override
    public final Mono<UniverseRootsResponse> getUniverseRoots(final String serverAddress) {
        logger.info("Get universe roots from tapd");
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(serverAddress)
                .build()
                .get()
                .uri("universe/roots")
                .exchangeToFlux(response -> response.bodyToFlux(UniverseRootsResponse.class))
                .next();
    }

    @Override
    public final Mono<UniverseLeavesResponse> getUniverseLeaves(final String serverAddress,
                                                                final String assetId) {
        logger.info("Get universe leaves from tapd for asset {}", assetId);
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(serverAddress)
                .build()
                .get()
                .uri("universe/leaves/asset-id/" + assetId)
                .exchangeToFlux(response -> response.bodyToFlux(UniverseLeavesResponse.class))
                .next();
    }

}

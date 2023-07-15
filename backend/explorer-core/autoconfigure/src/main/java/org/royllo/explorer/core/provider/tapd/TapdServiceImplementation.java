package org.royllo.explorer.core.provider.tapd;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.BaseProviderService;
import org.royllo.explorer.core.util.parameters.OutgoingRateLimitsParameters;
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
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class TapdServiceImplementation extends BaseProviderService implements TapdService {

    /** Webflux codec maximum size. */
    public static final int CODE_MAXIMUM_SIZE = 16 * 1024 * 1024;

    /** Outgoing rate limits parameters. */
    private final OutgoingRateLimitsParameters outgoingRateLimitsParameters;

    /** TAPD parameters. */
    private final TAPDParameters tapdParameters;
    /** SSL Context. */
    private SslContext sslContext;

    /**
     * Post construct.
     */
    @PostConstruct
    private void postConstruct() {
        bucket = Bucket.builder()
                .addLimit(Bandwidth.simple(1, outgoingRateLimitsParameters.getDelayBetweenRequests()))
                .build();
    }

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
    public final Mono<DecodedProofResponse> decode(final String rawProof) {
        logger.info("Calling decode for proof from tapd with raw proof {}", rawProof);
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));

        // Consume a token from the token bucket.
        // If a token is not available this method will block until the refill adds one to the bucket.
        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(tapdParameters.getApi().getBaseUrl())
                .build()
                .post()
                .uri("/v1/taproot-assets/proofs/decode")
                .header("Grpc-Metadata-macaroon", tapdParameters.getApi().getMacaroon())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ProofRequest.builder()
                        .rawProof(rawProof)
                        .withPrevWitnesses(false)
                        .withMetaReveal(false)
                        .build()
                ))
                .exchangeToFlux(response -> response.bodyToFlux(DecodedProofResponse.class))
                .next();
    }

    @Override
    public final Mono<UniverseRootsResponse> getUniverseRoots(final String serverAddress) {
        logger.info("Get universe roots from tapd server: {}", serverAddress);
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));

        // Consume a token from the token bucket.
        // If a token is not available this method will block until the refill adds one to the bucket.
        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return WebClient.builder()
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(CODE_MAXIMUM_SIZE))
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(serverAddress)
                .build()
                .get()
                .uri("/v1/taproot-assets/universe/roots")
                .exchangeToFlux(response -> response.bodyToFlux(UniverseRootsResponse.class))
                .next();
    }

    @Override
    public final Mono<UniverseLeavesResponse> getUniverseLeaves(final String serverAddress,
                                                                final String assetId) {
        logger.info("Get universe leaves from tapd for asset {}", assetId);
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(getSslContext()));

        // Consume a token from the token bucket.
        // If a token is not available this method will block until the refill adds one to the bucket.
        try {
            bucket.asBlocking().consume(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(serverAddress)
                .build()
                .get()
                .uri("/v1/taproot-assets/universe/leaves/asset-id/" + assetId)
                .exchangeToFlux(response -> response.bodyToFlux(UniverseLeavesResponse.class))
                .next();
    }

}

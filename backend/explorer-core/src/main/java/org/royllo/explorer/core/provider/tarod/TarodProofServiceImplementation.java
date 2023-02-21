package org.royllo.explorer.core.provider.tarod;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.util.base.BaseMempoolService;
import org.royllo.explorer.core.util.parameters.TarodParameters;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.List;

/**
 * Tarod proof service implementation.
 */
@Service
@RequiredArgsConstructor
public class TarodProofServiceImplementation extends BaseMempoolService implements TarodProofService {

    /** Tarod parameters. */
    private final TarodParameters tarodParameters;

    @Override
    public final Mono<DecodedProofResponse> decode(final String rawProof, final long proofIndex) {
        SslContext sslContext = null;
        try {
            sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        SslContext finalSslContext = sslContext;
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(finalSslContext));



        logger.info("Calling decode for proof n°{} with raw proof {}", proofIndex, rawProof);
        return WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(tarodParameters.getApi().getBaseUrl())
                .build()
                .post()
                .uri("proofs/decode")
                .header("Grpc-Metadata-macaroon", tarodParameters.getApi().getMacaroon())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ProofDTO.builder()
                        .rawProof("looo")
                        .proofIndex(1)
                        .build()
                ))
                .retrieve()
                .bodyToMono(DecodedProofResponse.class)
                .doOnError(throwable -> logger.error("Calling decode for proof n°{} with raw proof {}: {}",
                        proofIndex,
                        rawProof,
                        throwable.getMessage()));
//                .onStatus(HttpStatus::isError, clientResponse -> {
//                    return Mono.error(new Exception("error"));
//                })
//                .bodyToMono(DecodedProofResponse.class);
//                //.retryWhen(defaultRetryConfiguration);



    }

}

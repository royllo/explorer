package org.royllo.explorer.core.test.integration.tarod;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.provider.tarod.DecodedProofResponse;
import org.royllo.explorer.core.provider.tarod.TarodProofService;
import org.royllo.explorer.core.test.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@DisplayName("Tarod proof service test")
public class TarodProofServiceTest extends BaseTest {

    @Autowired
    private TarodProofService tarodProofService;
//    @Container
//    public static DockerComposeContainer environment = new DockerComposeContainer(new File("src/test/resources/docker-compose-lnd-and-taro.yml"))
//            .withOptions("--compatibility")
//            .waitingFor("taro_1", Wait.forLogMessage(".*Taro Daemon fully active!.*\\n", 1));

    @Test
    @DisplayName("decode()")
    @SuppressWarnings("SpellCheckingInspection")
    public void decodeTest() {
        //System.out.println("ICI");
        //assertTrue(true);

        final Mono<DecodedProofResponse> response = tarodProofService.decode("dd", 1);
        System.out.println("==> " + response.block());

    }

}

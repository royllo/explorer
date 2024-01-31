package org.royllo.explorer.core.service.util;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.domain.util.K1Value;
import org.royllo.explorer.core.repository.util.K1ValueRepository;
import org.tbk.lnurl.auth.K1;
import org.tbk.lnurl.auth.K1Factory;
import org.tbk.lnurl.auth.K1Manager;
import org.tbk.lnurl.auth.SimpleK1Factory;
import org.tbk.lnurl.simple.auth.SimpleK1;

import java.time.ZonedDateTime;

/**
 * Database K1 manager.
 */
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:DesignForExtension"})
public class DatabaseK1Manager implements K1Manager {

    /** Simple K1 factory (Generated with random). */
    private final K1Factory factory = new SimpleK1Factory();

    /** K1 value repository. */
    private final K1ValueRepository repository;

    @Override
    public boolean isValid(final K1 k1) {
        // Purge old k1 before searching for it.
        repository.findByCreatedOnBefore(ZonedDateTime.now().minusHours(1))
                .stream()
                .map(k1Value -> SimpleK1.fromHex(k1Value.getK1()))
                .forEach(this::invalidate);

        return repository.existsById(k1.toHex());
    }

    @Override
    public void invalidate(final K1 k1) {
        repository.delete(K1Value.builder().k1(k1.toHex()).build());
    }

    @Override
    public K1 create() {
        K1 k1 = factory.create();
        repository.save(K1Value.builder().k1(k1.toHex()).build());
        return k1;
    }

}

package org.royllo.explorer.batch.util.base;

import jakarta.annotation.PreDestroy;
import org.royllo.explorer.core.util.base.Base;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for batch.
 */
public class BaseBatch extends Base {

    /** Batch continues to run as long as enabled is set to true. */
    protected final AtomicBoolean enabled = new AtomicBoolean(true);

    /**
     * This method is called before the application shutdown.
     * We stop doing the batch.
     */
    @PreDestroy
    public void shutdown() {
        logger.info("Batch closing gracefully...");
        enabled.set(false);
    }

}

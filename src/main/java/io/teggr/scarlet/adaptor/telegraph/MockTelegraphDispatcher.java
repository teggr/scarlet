package io.teggr.scarlet.adaptor.telegraph;

import io.teggr.scarlet.domain.conduit.TelegraphicDispatcher;
import io.teggr.scarlet.domain.nucleus.TransmissionEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockTelegraphDispatcher implements TelegraphicDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(MockTelegraphDispatcher.class);

    @Override
    public void propagate(TransmissionEnvelope envelope) {
        logger.info("MOCK: Would send to node {} via Telegram: {}", 
            envelope.destinationNode(), envelope.payloadContent());
    }
}

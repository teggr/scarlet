package io.teggr.scarlet.domain.conduit;

import io.teggr.scarlet.domain.nucleus.TransmissionEnvelope;

public interface TelegraphicDispatcher {
    void propagate(TransmissionEnvelope envelope);
}

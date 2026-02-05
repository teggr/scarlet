package io.teggr.scarlet.domain.nucleus;

import java.util.Objects;

public final class TransmissionEnvelope {
    private final String destinationNode;
    private final String payloadContent;

    public TransmissionEnvelope(String destinationNode, String payloadContent) {
        this.destinationNode = Objects.requireNonNull(destinationNode, "destinationNode cannot be null");
        this.payloadContent = Objects.requireNonNull(payloadContent, "payloadContent cannot be null");
    }

    public String destinationNode() {
        return destinationNode;
    }

    public String payloadContent() {
        return payloadContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TransmissionEnvelope other)) return false;
        return Objects.equals(destinationNode, other.destinationNode) &&
               Objects.equals(payloadContent, other.payloadContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationNode, payloadContent);
    }
}

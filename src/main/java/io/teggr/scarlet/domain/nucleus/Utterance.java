package io.teggr.scarlet.domain.nucleus;

import java.time.Instant;
import java.util.Objects;

public final class Utterance {
    private final String ephemeralToken;
    private final String inscription;
    private final String originatorTag;
    private final Instant momentOfCreation;
    private final FlowDirection trajectory;
    private final String conversationThread;

    public Utterance(String ephemeralToken, String inscription, String originatorTag, 
                     Instant momentOfCreation, FlowDirection trajectory, String conversationThread) {
        this.ephemeralToken = ephemeralToken;
        this.inscription = Objects.requireNonNull(inscription, "inscription cannot be null");
        this.originatorTag = Objects.requireNonNull(originatorTag, "originatorTag cannot be null");
        this.momentOfCreation = Objects.requireNonNull(momentOfCreation, "momentOfCreation cannot be null");
        this.trajectory = Objects.requireNonNull(trajectory, "trajectory cannot be null");
        this.conversationThread = conversationThread;
    }

    public String ephemeralToken() {
        return ephemeralToken;
    }

    public String inscription() {
        return inscription;
    }

    public String originatorTag() {
        return originatorTag;
    }

    public Instant momentOfCreation() {
        return momentOfCreation;
    }

    public FlowDirection trajectory() {
        return trajectory;
    }

    public String conversationThread() {
        return conversationThread;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Utterance other)) return false;
        return Objects.equals(ephemeralToken, other.ephemeralToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ephemeralToken);
    }

    @Override
    public String toString() {
        return "Utterance{" +
                "ephemeralToken='" + ephemeralToken + '\'' +
                ", inscription='" + inscription + '\'' +
                ", originatorTag='" + originatorTag + '\'' +
                ", momentOfCreation=" + momentOfCreation +
                ", trajectory=" + trajectory +
                ", conversationThread='" + conversationThread + '\'' +
                '}';
    }
}

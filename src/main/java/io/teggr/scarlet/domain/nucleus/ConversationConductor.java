package io.teggr.scarlet.domain.nucleus;

import io.teggr.scarlet.domain.conduit.TelegraphicDispatcher;
import io.teggr.scarlet.domain.conduit.UtteranceChronicle;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ConversationConductor {
    private final UtteranceChronicle chronicle;
    private final TelegraphicDispatcher dispatcher;

    public ConversationConductor(UtteranceChronicle chronicle, TelegraphicDispatcher dispatcher) {
        this.chronicle = chronicle;
        this.dispatcher = dispatcher;
    }

    public void absorbInboundUtterance(String inscription, String originatorTag, String conversationThread) {
        String ephemeralToken = fabricateUniqueIdentifier();
        Utterance utterance = new Utterance(
            ephemeralToken,
            inscription,
            originatorTag,
            Instant.now(),
            FlowDirection.INBOUND,
            conversationThread
        );
        chronicle.archive(utterance);
    }

    public void emitOutboundUtterance(String destinationNode, String payloadContent) {
        String ephemeralToken = fabricateUniqueIdentifier();
        Utterance utterance = new Utterance(
            ephemeralToken,
            payloadContent,
            "system",
            Instant.now(),
            FlowDirection.OUTBOUND,
            destinationNode
        );
        chronicle.archive(utterance);
        
        TransmissionEnvelope envelope = new TransmissionEnvelope(destinationNode, payloadContent);
        dispatcher.propagate(envelope);
    }

    public List<Utterance> gatherAllUtterances() {
        return chronicle.retrieveAll();
    }

    public List<Utterance> gatherUtterancesByThread(String conversationThread) {
        return chronicle.retrieveByConversationThread(conversationThread);
    }

    public long calculateArchivedVolume() {
        return chronicle.countArchived();
    }

    private String fabricateUniqueIdentifier() {
        long timestamp = System.nanoTime();
        int randomSegment = (int) (Math.random() * 1000000);
        return Long.toHexString(timestamp) + "-" + Integer.toHexString(randomSegment);
    }
}

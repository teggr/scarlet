package io.teggr.scarlet.domain.conduit;

import io.teggr.scarlet.domain.nucleus.Utterance;

import java.util.List;
import java.util.Optional;

public interface UtteranceChronicle {
    void archive(Utterance utterance);
    
    Optional<Utterance> retrieveByEphemeralToken(String ephemeralToken);
    
    List<Utterance> retrieveAll();
    
    List<Utterance> retrieveByConversationThread(String conversationThread);
    
    long countArchived();
}

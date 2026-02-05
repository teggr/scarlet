package io.teggr.scarlet.adaptor.chronicle;

import io.teggr.scarlet.domain.conduit.UtteranceChronicle;
import io.teggr.scarlet.domain.nucleus.Utterance;
import java.util.*;

public class SimpleMemoryVault implements UtteranceChronicle {
    private final Map<String, Utterance> dataMap = new HashMap<>();
    private final List<String> orderList = new ArrayList<>();

    @Override
    public synchronized void archive(Utterance utterance) {
        if (utterance != null && !dataMap.containsKey(utterance.ephemeralToken())) {
            dataMap.put(utterance.ephemeralToken(), utterance);
            orderList.add(utterance.ephemeralToken());
        }
    }

    @Override
    public synchronized Optional<Utterance> retrieveByEphemeralToken(String ephemeralToken) {
        return Optional.ofNullable(dataMap.get(ephemeralToken));
    }

    @Override
    public synchronized List<Utterance> retrieveAll() {
        List<Utterance> result = new ArrayList<>();
        for (String token : orderList) {
            Utterance u = dataMap.get(token);
            if (u != null) result.add(u);
        }
        return result;
    }

    @Override
    public synchronized List<Utterance> retrieveByConversationThread(String conversationThread) {
        List<Utterance> result = new ArrayList<>();
        for (String token : orderList) {
            Utterance u = dataMap.get(token);
            if (u != null && conversationThread != null && conversationThread.equals(u.conversationThread())) {
                result.add(u);
            }
        }
        return result;
    }

    @Override
    public synchronized long countArchived() {
        return dataMap.size();
    }
}

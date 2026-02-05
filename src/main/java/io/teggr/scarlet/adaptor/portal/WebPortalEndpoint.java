package io.teggr.scarlet.adaptor.portal;

import io.teggr.scarlet.domain.nucleus.ConversationConductor;
import io.teggr.scarlet.domain.nucleus.FlowDirection;
import io.teggr.scarlet.domain.nucleus.Utterance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aperture")
public class WebPortalEndpoint {
    private final ConversationConductor conductor;

    public WebPortalEndpoint(ConversationConductor conductor) {
        this.conductor = conductor;
    }

    @GetMapping("/utterances")
    public ResponseEntity<List<UtteranceDTO>> fetchAllUtterances() {
        List<Utterance> utterances = conductor.gatherAllUtterances();
        List<UtteranceDTO> dtos = utterances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/utterances/thread/{threadId}")
    public ResponseEntity<List<UtteranceDTO>> fetchUtterancesByThread(@PathVariable String threadId) {
        List<Utterance> utterances = conductor.gatherUtterancesByThread(threadId);
        List<UtteranceDTO> dtos = utterances.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/transmit")
    public ResponseEntity<Void> transmitOutbound(@RequestBody TransmissionRequest request) {
        conductor.emitOutboundUtterance(request.destinationNode, request.payloadContent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> fetchStatistics() {
        long totalCount = conductor.calculateArchivedVolume();
        return ResponseEntity.ok(new StatisticsDTO(totalCount));
    }

    private UtteranceDTO convertToDTO(Utterance utterance) {
        return new UtteranceDTO(
                utterance.ephemeralToken(),
                utterance.inscription(),
                utterance.originatorTag(),
                utterance.momentOfCreation().toString(),
                utterance.trajectory().name(),
                utterance.conversationThread()
        );
    }

    public static class UtteranceDTO {
        public String ephemeralToken;
        public String inscription;
        public String originatorTag;
        public String momentOfCreation;
        public String trajectory;
        public String conversationThread;

        public UtteranceDTO(String ephemeralToken, String inscription, String originatorTag,
                           String momentOfCreation, String trajectory, String conversationThread) {
            this.ephemeralToken = ephemeralToken;
            this.inscription = inscription;
            this.originatorTag = originatorTag;
            this.momentOfCreation = momentOfCreation;
            this.trajectory = trajectory;
            this.conversationThread = conversationThread;
        }
    }

    public static class TransmissionRequest {
        public String destinationNode;
        public String payloadContent;
    }

    public static class StatisticsDTO {
        public long totalArchivedVolume;

        public StatisticsDTO(long totalArchivedVolume) {
            this.totalArchivedVolume = totalArchivedVolume;
        }
    }
}

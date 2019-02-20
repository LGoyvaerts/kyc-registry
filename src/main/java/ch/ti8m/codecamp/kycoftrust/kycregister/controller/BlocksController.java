package ch.ti8m.codecamp.kycoftrust.kycregister.controller;

import ch.ti8m.codecamp.kycoftrust.kycregister.domain.BlockRequest;
import ch.ti8m.codecamp.kycoftrust.kycregister.domain.UsernameEntity;
import ch.ti8m.codecamp.kycoftrust.kycregister.service.UsernameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BlocksController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UsernameService usernameService;

    @MessageMapping("/unregister/{uuId}")
    public void unregister(@Payload String message, @DestinationVariable("uuId") String uuId) {
        log.info("[/unregister] Client with UUID = " + uuId + " is unregistering himself.");
        usernameService.removeUsernameByName(uuId);
    }

    @MessageMapping("/addBlock")
    @SendTo("/topic/newBlock")
    public String addBlock(String message) {
        log.info("[/addBlock] New Block received from Client.");
        return message;
    }

    @MessageMapping("/getBlocks/{uuId}/{startIndex}")
    public void getBlocksFromFirstUsername(@Payload String message, @DestinationVariable("uuId") String uuId, @DestinationVariable("startIndex") String startIndex) throws JsonProcessingException {
        UsernameEntity firstUsername = usernameService.getFirstUsername();
        if (firstUsername != null) {
            String targetUuid = "";
            log.info("[/getBlocks] Client with UUID = " + uuId + " is getting Blocks with startIndex = " + startIndex);
            BlockRequest blockRequest = new BlockRequest(Long.parseLong(startIndex), uuId);
            String blockRequestAsString = objectMapper.writeValueAsString(blockRequest);
            simpMessagingTemplate.setDefaultDestination("/topic");
            if (firstUsername.getUsername().equals(uuId)) {
                if (usernameService.getSecondUsername() != null) {
                    targetUuid = usernameService.getSecondUsername().getUsername();
                }
            } else {
                targetUuid = firstUsername.getUsername();
            }
            simpMessagingTemplate.convertAndSend("/topic/getBlocks/" + targetUuid, blockRequestAsString);

        } else {
            log.error("[/getBlocks] First Username was not found.");
        }
    }

    @MessageMapping("/setBlocks/{uuId}")
    @SendTo("/topic/setBlocks/{uuId}")
    public String setBlocks(@Payload String message, @DestinationVariable("uuId") String uuId) {
        log.info("[/setBlocks] Blocks of Client with UUID = " + uuId + " are being set");
        return message;
    }

    @MessageMapping("/invalidate")
    @SendTo("/topic/invalidate")
    public String invalidateBlock(@Payload String message) {
        log.info("[/invalidate] Block invalidated. Block: " + message);
        return message;
    }
}

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

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
        log.info("[/unregister/" + uuId + "] Client is unregistering himself.");
        UsernameEntity usernameEntityToAlert = usernameService.getUsernameByUsername(uuId);
        usernameService.removeUsernameByName(uuId);

        if (usernameEntityToAlert.getGenesisHash() != null) {
            if (!usernameService.isGenesisStillExisting(uuId)) {
                log.info("[/unregister] Genesis is getting deleted: " + usernameEntityToAlert.getGenesisHash());
                alertGenesisDeletion(usernameEntityToAlert.getGenesisHash());
            }
        }
    }

    @MessageMapping("/addBlock/{genesisHash}")
    @SendTo("/topic/newBlock/{genesisHash}")
    public String addBlock(@Payload String message, @DestinationVariable("genesisHash") String genesisHash) {
        log.info("[/addBlock/" + genesisHash + "] New Block received from Client.");
        return message;
    }

    @MessageMapping("/getBlocks/{genesisHash}/{uuId}/{startIndex}")
    public void getBlocksFromFirstUsername(@Payload String message, @DestinationVariable("genesisHash") String genesisHash, @DestinationVariable("uuId") String uuId, @DestinationVariable("startIndex") String startIndex) {
        try {
            simpMessagingTemplate.setDefaultDestination("/topic");
            BlockRequest blockRequest = new BlockRequest(Long.parseLong(startIndex), uuId);
            String blockRequestAsString = objectMapper.writeValueAsString(blockRequest);

            String targetUuid = usernameService.getTargetUuid(genesisHash, uuId);

            if (targetUuid != null) {
                simpMessagingTemplate.convertAndSend("/topic/getBlocks/" + targetUuid, blockRequestAsString);
            }
        } catch (JsonProcessingException e) {
            log.error("[/getBlocks] Error Processing JSON: " + e.getMessage());
        }
    }

    @MessageMapping("/setBlocks/{uuId}")
    @SendTo("/topic/setBlocks/{uuId}")
    public String setBlocks(@Payload String message, @DestinationVariable("uuId") String uuId) {
        log.info("[/setBlocks] Blocks of Client with UUID = " + uuId + " are being set");
        return message;
    }

    @MessageMapping("/invalidate/{genesisHash}")
    @SendTo("/topic/invalidate/{genesisHash}")
    public String invalidateBlock(@Payload String message, @DestinationVariable("genesisHash") String genesisHash) {
        log.info("[/invalidate/" + genesisHash + "] Block invalidated. Block: " + message);
        return message;
    }

    @MessageMapping("/getActiveGenesises/{uuId}")
    public void sendActiveGenesisesToClient(@DestinationVariable("uuId") String uuId) {
        log.info("[/getActiveGenesises/" + uuId + "] Client is requesting active Genesises");

        List<UsernameEntity> usernameEntities = usernameService.getActiveGenesises();

        try {
            simpMessagingTemplate.convertAndSend("/topic/setActiveGenesises/" + uuId, objectMapper.writeValueAsString(usernameEntities));
        } catch (JsonProcessingException e) {
            log.error("[/topic/setActiveGenesises/" + uuId + "] Could not send active Genesises to Client: " + e.getMessage());
        }
    }

    @MessageMapping("/setGenesis/{uuId}")
    public void setGenesisOfClient(@Payload String genesisAsString, @DestinationVariable("uuId") String uuId) {

        try {
            log.info("[/setGenesis/" + uuId + "] Setting Genesis to UUID");
            UsernameEntity usernameWithGenesis = objectMapper.readValue(genesisAsString, UsernameEntity.class);

            if (!usernameService.isGenesisAlreadyExisting(usernameWithGenesis)) {
                alertNewGenesis(usernameWithGenesis);
            } else {
                log.info("Genesis is already existing. Adding Client to Active Genesis List.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 5000L, fixedDelay = 5000L)
    public void pingToClient() {
        simpMessagingTemplate.setDefaultDestination("/topic");

        List<UsernameEntity> unresponsiveUsernameEntityList = usernameService.getUnresponsiveList();
        for (UsernameEntity usernameEntityToAlert : unresponsiveUsernameEntityList) {
            log.info("[PING] Removing Username from Registry: " + usernameEntityToAlert.getUsername());
            alertUsernameDeletion(usernameEntityToAlert.getGenesisHash());

            if (!usernameService.isGenesisStillExisting(usernameEntityToAlert)) {
                alertGenesisDeletion(usernameEntityToAlert.getGenesisHash());
            }

        }
        simpMessagingTemplate.convertAndSend("/topic/ping", "ping");

    }

    @MessageMapping("/pong/{uuId}")
    public void receivePongFromClient(@Payload String message, @DestinationVariable("uuId") String uuId) {
        UsernameEntity usernameEntity = new UsernameEntity();
        usernameEntity.setUsername(uuId);

        usernameService.saveUsername(usernameEntity);
    }

    public void alertUsernameDeletion(String username) {
        simpMessagingTemplate.setDefaultDestination("/topic");
        simpMessagingTemplate.convertAndSend("/topic/removeUsername/" + username, "Ba Bye");
    }

    public void alertGenesisDeletion(String genesisHash) {
        simpMessagingTemplate.setDefaultDestination("/topic");
        simpMessagingTemplate.convertAndSend("/topic/removeGenesis", genesisHash);
    }

    public void alertNewGenesis(UsernameEntity genesisEntity) {
        try {
            UsernameEntity genesisWithStamps = usernameService.getUsernameByUsername(genesisEntity.getUsername());
            simpMessagingTemplate.setDefaultDestination("/topic");
            simpMessagingTemplate.convertAndSend("/topic/newGenesis", objectMapper.writeValueAsString(genesisWithStamps));
            log.info("[/publishgenesis] New Genesis published. Hash: " + genesisEntity.getGenesisHash());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

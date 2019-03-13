package ch.ti8m.codecamp.kycoftrust.kycregister.service;

import ch.ti8m.codecamp.kycoftrust.kycregister.domain.UsernameEntity;
import ch.ti8m.codecamp.kycoftrust.kycregister.repo.UsernameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsernameService {

    private final static Logger log = LoggerFactory.getLogger(UsernameService.class);

    @Autowired
    private UsernameRepository usernameRepository;

    public UsernameEntity getUsernameByUsername(String username) {
        Optional<UsernameEntity> usernameEntityOptional = usernameRepository.findByUsername(username);

        return usernameEntityOptional.orElse(null);
    }

    public String getTargetUuid(String genesisHash, String uuId) {
        UsernameEntity firstUsername = getFirstUsernameWithGenesisHash(genesisHash);
        if (firstUsername != null) {
            String targetUuid = "";
            if (firstUsername.getUsername().equals(uuId)) {
                UsernameEntity secondUsername = getSecondUsernameWithGenesisHash(genesisHash);
                if (secondUsername != null) {
                    targetUuid = secondUsername.getUsername();
                }
            } else {
                targetUuid = firstUsername.getUsername();
            }

            return targetUuid;
        } else {
            log.error("[UsernameService] First Username was not found.");
            return null;
        }
    }

    public UsernameEntity getFirstUsernameWithGenesisHash(String genesisHash) {
        Optional<UsernameEntity> username = usernameRepository.findFirstByGenesisHash(genesisHash);
        if (username.isPresent()) {
            return username.get();
        } else {
            log.info("[UsernameService] Can not find first Username in Username Repository by GenesisHash: " + genesisHash);
            return null;
        }
    }

    public UsernameEntity getSecondUsernameWithGenesisHash(String genesisHash) {
        List<UsernameEntity> usernameEntities = usernameRepository.findFirst2ByGenesisHash(genesisHash);
        if (!usernameEntities.isEmpty() && usernameEntities.size() > 1) {
            return usernameEntities.get(1);
        } else {
            log.error("[UsernameService] No second Username found by GenesisHash: " + genesisHash);
        }
        return null;
    }

    public UsernameEntity getFirstUsername() {
        Optional<UsernameEntity> username = usernameRepository.findFirstByOrderById();
        if (username.isPresent()) {
            return username.get();
        } else {
            log.info("[UsernameService] Can not find first Username in Username Repository");
            return null;
        }
    }

    public UsernameEntity getSecondUsername() {
        List<UsernameEntity> usernameEntities = usernameRepository.findFirst2ByOrderById();
        if (!usernameEntities.isEmpty() && usernameEntities.size() > 1) {
            return usernameEntities.get(1);
        } else {
            log.error("[UsernameService] No second Username found.");
        }
        return null;
    }

    public Page<UsernameEntity> getUsernames(Pageable pageable) {
        return usernameRepository.findAll(pageable);
    }

    public UsernameEntity saveUsername(UsernameEntity username) {
        Optional<UsernameEntity> byUsername = usernameRepository.findByUsername(username.getUsername());
        if (byUsername.isPresent()) {
            byUsername.get().setRegistred(new Date());
            return usernameRepository.save(byUsername.get());
        } else {
            username.setRegistred(new Date());
            return usernameRepository.save(username);
        }
    }

    public List<UsernameEntity> getActiveGenesises() {
        Iterable<UsernameEntity> usernameEntitiesWithGenesisHashIterable = usernameRepository.findByGenesisHashNotNull();
        List<UsernameEntity> uniqueGenesisUsernameList = new ArrayList<>();

        for (UsernameEntity usernameEntity : usernameEntitiesWithGenesisHashIterable) {
            boolean isContained = false;
            for (UsernameEntity uniqueUsername : uniqueGenesisUsernameList) {
                if (uniqueUsername.getGenesisHash().equals(usernameEntity.getGenesisHash())) {
                    isContained = true;
                }
            }
            if (!isContained) {
                uniqueGenesisUsernameList.add(usernameEntity);
            }
        }

        return uniqueGenesisUsernameList;
    }

    public UsernameEntity saveGenesis(UsernameEntity usernameEntity) {
        Optional<UsernameEntity> byUsername = usernameRepository.findByUsername(usernameEntity.getUsername());
        if (byUsername.isPresent()) {
            byUsername.get().setRegistred(new Date());
            byUsername.get().setGenesisHash(usernameEntity.getGenesisHash());
            byUsername.get().setGenesisName(usernameEntity.getGenesisName());
            byUsername.get().setGenesisImagepath(usernameEntity.getGenesisImagepath());
            byUsername.get().setGenesisCreatedOn(new Date());
            return usernameRepository.save(byUsername.get());
        } else {
            log.error("[UsernameService] Username was not found on Genesis save.");
            return null;
        }
    }

    public List<UsernameEntity> getUnresponsiveList() {
        Iterable<UsernameEntity> usernameEntityIterable = findAll();
        List<UsernameEntity> unresponsiveUsernameList = new ArrayList<>();

        for (UsernameEntity usernameEntity : usernameEntityIterable) {
            long deadlineInMilliSeconds = new Date().getTime() - usernameEntity.getRegistred().getTime();
            if (deadlineInMilliSeconds > 6000) {
                unresponsiveUsernameList.add(usernameEntity);
                deleteUsername(usernameEntity);
            }
        }

        return unresponsiveUsernameList;
    }

    public boolean isGenesisAlreadyExisting(UsernameEntity usernameEntityToCheck) {
        Iterable<UsernameEntity> usernamesWithGenesisIterable = getActiveGenesises();

        boolean isExisting = false;
        for (UsernameEntity currentGenesis : usernamesWithGenesisIterable) {
            if (currentGenesis.getGenesisHash().equals(usernameEntityToCheck.getGenesisHash())) {
                isExisting = true;
            }
        }
        saveGenesis(usernameEntityToCheck);
        return isExisting;

    }

    public UsernameEntity alreadyExistingGenesis(UsernameEntity usernameEntityToCheck) {
        Iterable<UsernameEntity> usernamesWithGenesisIterable = getActiveGenesises();

        boolean isExisting = false;
        for (UsernameEntity currentGenesis : usernamesWithGenesisIterable) {
            if (currentGenesis.getGenesisHash().equals(usernameEntityToCheck.getGenesisHash())) {
                isExisting = true;
            }
        }

        UsernameEntity savedUsername = saveGenesis(usernameEntityToCheck);
        if (!isExisting) {
            return savedUsername;
        } else {
            return null;
        }
    }

    public boolean isGenesisStillExisting(UsernameEntity usernameEntityToCheck) {
        boolean isStillExisting = false;
        List<UsernameEntity> activeGenesises = getActiveGenesises();
        for (UsernameEntity activeGenesis : activeGenesises) {
            if (activeGenesis.getGenesisHash().equals(usernameEntityToCheck.getGenesisHash())) {
                isStillExisting = true;
            }
        }

        return isStillExisting;
    }

    public boolean isGenesisStillExisting(String username) {
        Optional<UsernameEntity> usernameEntityOptional = usernameRepository.findByUsername(username);
        boolean isStillExisting = false;

        if (usernameEntityOptional.isPresent()) {
            UsernameEntity usernameEntityToCheck = usernameEntityOptional.get();
            List<UsernameEntity> activeGenesises = getActiveGenesises();
            for (UsernameEntity activeGenesis : activeGenesises) {
                if (activeGenesis.getGenesisHash().equals(usernameEntityToCheck.getGenesisHash())) {
                    isStillExisting = true;
                }
            }
        }

        if (!isStillExisting) {
            usernameEntityOptional.ifPresent(this::deleteUsername);
        }

        return isStillExisting;
    }

    public void removeUsernameById(Long usernameId) {
        usernameRepository.findById(usernameId).ifPresent(usernameRepository::delete);
    }

    public void removeUsernameByName(String username) {
        usernameRepository.findByUsername(username).ifPresent(usernameRepository::delete);
    }

    public void deleteUsername(UsernameEntity usernameEntity) {
        usernameRepository.delete(usernameEntity);
    }

    public Iterable<UsernameEntity> findAll() {
        return usernameRepository.findAll();
    }

    public void deleteAll() {
        usernameRepository.deleteAll();
    }
}

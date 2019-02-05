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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsernameService {

    private final static Logger log = LoggerFactory.getLogger(UsernameService.class);

    @Autowired
    private UsernameRepository usernameRepository;

    public UsernameEntity getFirstUsername() {
        Optional<UsernameEntity> username = usernameRepository.findFirstByOrderById();
        if (username.isPresent()) {
            return username.get();
        } else {
            log.info("Can not find first Username in Username Repository");
            return null;
        }
    }

    public UsernameEntity getSecondUsername() {
        List<UsernameEntity> usernameEntities = usernameRepository.findFirst2ByOrderById();
        if (!usernameEntities.isEmpty() && usernameEntities.size() > 1) {
            return usernameEntities.get(1);
        } else {
            log.error("No second Username found.");
        }
        return null;
    }

    public Page<UsernameEntity> getUsernames(Pageable pageable) {
        return usernameRepository.findAll(pageable);
    }

    public UsernameEntity saveUsername(UsernameEntity username) {
        Optional<UsernameEntity> byUsername = usernameRepository.findByUsername(username.getUsername());
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            username.setRegistred(ZonedDateTime.now());
            return usernameRepository.save(username);
        }
    }

    public void removeUsernameById(Long usernameId) {
        usernameRepository.findById(usernameId).ifPresent(usernameRepository::delete);
    }

    public void removeUsernameByName(String username) {
        usernameRepository.findByUsername(username).ifPresent(usernameRepository::delete);
    }

    public Iterable<UsernameEntity> findAll() {
        return usernameRepository.findAll();
    }

    public void deleteAll() {
        usernameRepository.deleteAll();
    }
}

package ch.ti8m.codecamp.kycoftrust.kycregister.repo;

import ch.ti8m.codecamp.kycoftrust.kycregister.domain.UsernameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsernameRepository extends PagingAndSortingRepository<UsernameEntity, Long> {

    @Override
    Optional<UsernameEntity> findById(Long aLong);

    Optional<UsernameEntity> findByUsername(String username);

    Optional<UsernameEntity> findFirstByOrderById();

    List<UsernameEntity> findFirst2ByOrderById();

    Optional<UsernameEntity> findFirstByGenesisHash(String genesisHash);

    List<UsernameEntity> findFirst2ByGenesisHash(String genesisHash);

    Iterable<UsernameEntity> findByGenesisHashNotNull();

    @Query("select u from UsernameEntity u where u.genesisHash = :genesisHash")
    List<UsernameEntity> findListByGenesisHash(@Param("genesisHash") String genesisHash);

    @Override
    void deleteAll();
}

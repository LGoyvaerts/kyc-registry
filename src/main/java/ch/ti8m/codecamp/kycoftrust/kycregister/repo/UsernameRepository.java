package ch.ti8m.codecamp.kycoftrust.kycregister.repo;

import ch.ti8m.codecamp.kycoftrust.kycregister.domain.UsernameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UsernameRepository extends PagingAndSortingRepository<UsernameEntity, Long> {

    @Override
    Optional<UsernameEntity> findById(Long aLong);

    Optional<UsernameEntity> findByUsername(String username);

    Optional<UsernameEntity> findFirstByOrderById();

    List<UsernameEntity> findFirst2ByOrderById();

    @Override
    void deleteAll();
}

package guru.springfamework.repositories;

import guru.springfamework.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mance on 09/05/2022.
 */
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}

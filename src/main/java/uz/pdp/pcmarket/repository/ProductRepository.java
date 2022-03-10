package uz.pdp.pcmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.pcmarket.entity.Attachment;
import uz.pdp.pcmarket.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByNameAndIdNot(String name,Integer id);
    boolean existsByName(String name);
}

package uz.pdp.pcmarket.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.pcmarket.entity.Product;
import uz.pdp.pcmarket.payload.ProductDTO;
import uz.pdp.pcmarket.repository.AttachmentRepository;
import uz.pdp.pcmarket.repository.CategoryRepository;
import uz.pdp.pcmarket.repository.ProductRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    final ProductRepository productRepository;
    final CategoryRepository categoryRepository;
    final AttachmentRepository attachmentRepository;

    @GetMapping
    public HttpEntity<?> all() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> one(@PathVariable Integer id) {
        if (productRepository.existsById(id)) {
            return ResponseEntity.ok().body(productRepository.getById(id));
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().body("deleted");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody ProductDTO dto) {

        if (!productRepository.existsByName(dto.getName())) {
            Product product = new Product();

            return getResponseEntity(dto, product);
        }
        return (HttpEntity<?>) ResponseEntity.noContent();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Integer id, @RequestBody ProductDTO dto) {

        if (productRepository.existsById(id)) {
            Product product = productRepository.getById(id);
            if (!productRepository.existsByNameAndIdNot(dto.getName(), id)) {
                return getResponseEntity(dto, product);
            }
        }
        return (ResponseEntity<?>) ResponseEntity.noContent();
    }

    @NotNull
    private ResponseEntity<?> getResponseEntity(@RequestBody ProductDTO dto, Product product) {
        product.setCategory(categoryRepository.getById(dto.getCategoryId()));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setPhoto(attachmentRepository.getById(dto.getAttachmentId()));

        Product save = productRepository.save(product);

        return ResponseEntity.ok().body("added\n" + save);
    }

}

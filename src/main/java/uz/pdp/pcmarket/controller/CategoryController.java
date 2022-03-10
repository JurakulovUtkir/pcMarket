package uz.pdp.pcmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.pcmarket.entity.Category;
import uz.pdp.pcmarket.repository.CategoryRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    final CategoryRepository categoryRepository;


    @GetMapping
    public HttpEntity<?> all() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> one(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Mahsulot o'chirildi\n" + categoryRepository.findAll());
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody Category category) {
        if (!categoryRepository.existsByName(category.getName())) {

            Category category1 = new Category();
            category1.setName(category.getName());
            Category save = categoryRepository.save(category1);

            return ResponseEntity.ok("Added successfully\n"+save);
        }
        return ResponseEntity.ok("already exists");
    }


    /**
     *  editing data
     * @param id
     * @param category
     * @return
     */
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Integer id,@RequestBody Category category){
        if (categoryRepository.existsById(id)) {
            if (!categoryRepository.existsByNameAndIdNot(category.getName(),id)) {

                Category category1 =categoryRepository.getById(id);
                category1.setName(category.getName());
                Category save = categoryRepository.save(category1);

                return ResponseEntity.ok("Edited data\n"+save);
            }
        }
        return ResponseEntity.notFound().build();
    }



}

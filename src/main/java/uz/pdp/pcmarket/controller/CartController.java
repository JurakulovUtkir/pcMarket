package uz.pdp.pcmarket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.pcmarket.entity.Cart;
import uz.pdp.pcmarket.repository.CartRepository;
import uz.pdp.pcmarket.repository.ProductRepository;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    final ProductRepository productRepository;

    final CartRepository cartRepository;

    @GetMapping("/{id}")
    public HttpEntity<?> allProductsInCart(@PathVariable Integer id) {

        return ResponseEntity.ok().body(cartRepository.getById(id).getProducts());
    }

    @DeleteMapping("/{cart_id}/{product_id}")
    public HttpEntity<?> delete(@PathVariable Integer cart_id, @PathVariable Integer product_id) {
        Cart cart = cartRepository.getById(cart_id);

        cart.getProducts().remove(productRepository.getById(product_id));

        cartRepository.save(cart);
        return ResponseEntity.ok().body("deleted product");
    }

    @PostMapping("/{cart_id}/{product_id}")
    public HttpEntity<?> add(@PathVariable Integer product_id, @PathVariable Integer cart_id) {
        Cart cart = cartRepository.getById(cart_id);

        cart.getProducts().add(productRepository.getById(product_id));

        cartRepository.save(cart);
        return ResponseEntity.ok().body("added product");
    }

}

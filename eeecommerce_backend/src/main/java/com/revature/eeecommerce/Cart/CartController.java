package com.revature.eeecommerce.Cart;

import com.revature.eeecommerce.Product.Product;
import com.revature.eeecommerce.Product.ProductService;
import com.revature.eeecommerce.User.UserService;
import com.revature.eeecommerce.util.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    // get cart by user (authorize logged-in user)
    // get all carts
    @GetMapping
    private ResponseEntity<List<Cart>> getCartByUserId(@Valid @RequestHeader int userId, @RequestHeader String userType){
        if (userId == 0) {
            throw new DataNotFoundException("This user does not exist or is not logged in");
        }
        else if(userType.equals("EMPLOYEE")) {
            return ResponseEntity.status(HttpStatus.OK).body(cartService.findAllCarts());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(cartService.findCartByUserId(userId));
        }
    }
    // post item to cart
    // update cart - Add more items
    @PostMapping
    private ResponseEntity<Cart> postCart(@Valid @RequestHeader int userId, @RequestParam int productId, @RequestParam int quantity) {
        if (userId == 0) {
            throw new DataNotFoundException("This user does not exist or is not logged in");
        } else {
            Cart newCart = new Cart();
            newCart.setProduct(productService.findById(productId));
            newCart.setUser(userService.findById(userId));
            newCart.setCount(quantity);
            newCart = cartService.postCart(newCart);
            return ResponseEntity.status(HttpStatus.OK).body(newCart);
        }
    }

    // delete by user id
    @DeleteMapping("/{cartId}")
    private ResponseEntity<Boolean> deleteCart(@Valid @PathVariable int cartId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.deleteCart(cartId));
    }

//    @GetMapping("/total")
//    private ResponseEntity<Void> getCartTotal(@Valid @RequestHeader int userId){
//        if (userId == 0) {
//            throw new DataNotFoundException("This user does not exist or is not logged in");
//        } else {
//            return ResponseEntity.noContent().build();
//        }
//    }
}

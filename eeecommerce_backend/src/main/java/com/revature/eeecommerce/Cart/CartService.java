package com.revature.eeecommerce.Cart;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService{
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    public List<Cart> findCartByUserId(int id) {
        return cartRepository.findCartByUserUserId(id);
    }

    @Transactional
    public Cart postCart(Cart cart) {
        Cart dupe = findCart(cart);
        if(dupe == null) {
            return cartRepository.save(cart);
        } else {
            if (dupe.getCount() + cart.getCount() > 0) {
                cartRepository.updateCart(dupe.getUser(), dupe.getProduct(), dupe.getCount() + cart.getCount());
            } else {
                cartRepository.deleteById(dupe.getCartId());
            }

            return cart;
        }
    }

    private Cart findCart(Cart cart) {
        Optional<Cart> cartOptional = cartRepository.findCartByUserUserIdAndProduct(cart.getUser().getUserId(), cart.getProduct());
        return cartOptional.orElse(null);
    }

//    @Transactional
//    public Integer deleteCartById(int userId) {
//        List<Cart> cartList = cartRepository.findCartByUserUserId(userId);
//        if(!cartList.isEmpty()) {
//            cartRepository.deleteByUserId(userId);
//            return 1;
//        } else {
//            return null;
//        }
//    }

    @Transactional
    public boolean deleteCart(int id) {
        cartRepository.deleteById(id);

        return true;
    }

//    public Integer getCartTotal(int userId) {
//        return cartRepository.getCartTotal(userId);
//    }
}

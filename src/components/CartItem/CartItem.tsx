import React from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';

// Types
import { CartItemType } from '../../types';

// Styles
import { Wrapper } from './CartItem.styles';

type Props = {
  item: CartItemType;
  addToCart: (clickedItem: CartItemType) => void;
  removeFromCart: (id: number) => void;
  fetchCartItems: () => void; // New prop to refresh cart
};

const CartItem: React.FC<Props> = ({ item, addToCart, removeFromCart, fetchCartItems }) => {
    const handleAddToCartWithRequest = async (item: CartItemType) => {
        const url = 'http://localhost:8080/cart';
        const params = new URLSearchParams();
        params.append('productId', item.id.toString());
        params.append('quantity', '1');

        const headers = {
        userId: 1, // Replace with actual userId if needed
        userType: "CUSTOMER"
        };

        try {
        await axios.post(url, params, { headers });
        console.log('Item added to cart');
        fetchCartItems(); // Fetch the updated cart items
        } catch (error) {
        console.error('Error adding item to cart:', error);
        }
  };

    const handleRemoveFromCartWithRequest = async (id: number) => {
        const url = 'http://localhost:8080/cart';
        const params = new URLSearchParams();
        params.append('productId', id.toString());
        params.append('quantity', '-1');

        const headers = {
        userId: '1', // Replace with actual userId if needed
        userType: "CUSTOMER"
        };

        try {
        await axios.post(url, params, { headers });
        console.log('Item removed from cart');
        fetchCartItems(); // Fetch the updated cart items
        } catch (error) {
        console.error('Error removing item from cart:', error);
        }
  };

  return (
    <Wrapper>
        <div>
            <h3>{item.title}</h3>
            <div className="information">
                <p>Price: ${item.price/100}</p>
                <p>Total: ${(item.amount * item.price/100).toFixed(2)}</p>
            </div>
            <div className="buttons">
                <Button size="small" disableElevation variant='contained' onClick={() => handleRemoveFromCartWithRequest(item.id)}>
                    -
                </Button>
                <p>{item.amount}</p>
                <Button size='small' disableElevation variant='contained' onClick={() => handleAddToCartWithRequest(item)}>
                +
                </Button>                
            </div>
        </div>
        <img src={item.image} alt={item.title} />
    </Wrapper>
  );
};

export default CartItem;

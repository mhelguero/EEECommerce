import React from 'react';
import Button from '@mui/material/Button';
import axios from 'axios';

// Types
import { CartItemType } from '../../types';

// Styles
import { Wrapper } from './Item.style';

type Props = {
  item: CartItemType;
  handleAddToCart: (clickedItem: CartItemType) => void;
  userId: number;
};

const Item: React.FC<Props> = ({ item, handleAddToCart, userId }) => {
  const handleAddToCartWithRequest = async (item: CartItemType) => {
    const url = 'http://3.141.164.75:8080/cart';
    const params = new URLSearchParams();
    params.append('productId', item.id.toString()); // Ensure the parameter name matches what the server expects
    params.append('quantity', '1'); // You can adjust the quantity as needed

    const headers = {
      userId: userId, // Replace with actual userId if needed
    };

    try {
      const response = await axios.post(url, params, { headers });
      console.log('Item added to cart:', response.data);
      handleAddToCart(item); // Call the original handleAddToCart prop function
    } catch (error) {
      console.error('Error adding item to cart:', error);
      // Handle the error as needed, e.g., show error message to the user
    }
  };

  return (
    <Wrapper>
      <img src={item.image} alt={item.title} />
      <div>
        <h3>{item.title}</h3>
        <p>{item.description}</p>
        <h3>${item.price / 100}</h3>
      </div>
      <Button onClick={() => handleAddToCartWithRequest(item)}>Add to cart</Button>
    </Wrapper>
  );
};

export default Item;

import React, { useEffect } from "react";
import axios from "axios";
import CartItem from "../CartItem/CartItem"; // Adjust the path as needed
import { CartItemType } from "../../types";
import { Wrapper } from "./Cart.styles"; // Add your styles here
import { Button } from "@mui/material";

type Props = {
  addToCart: (clickedItem: CartItemType) => void;
  removeFromCart: (id: number) => void;
  userId: number;
  fetchCartItems: () => void;
  cartItems: CartItemType[];
};

const Cart: React.FC<Props> = ({
  addToCart,
  removeFromCart,
  userId,
  fetchCartItems,
  cartItems,
}) => {
  useEffect(() => {
    fetchCartItems();
  }, []);

  const calculateTotal = (items: CartItemType[]) =>
    items.reduceRight((accumulator: number, item) => {
      console.log(
        item.discount,
        (
          accumulator +
          (item.amount * item.price * (1 - item.discount)) / 100
        ).toFixed(2)
      );
      return +(
        accumulator +
        (item.amount * item.price * (1 - item.discount)) / 100
      ).toFixed(2);
    }, 0);

  async function handleCheckout() {
    const header = {
      userId: userId,
    };
    const response = await axios({
      url: "http://localhost:8080/orders",
      method: "post",
      headers: header,
    });
  }

  return (
    <Wrapper>
      {cartItems.length > 0 ? (
        cartItems.map((item) => (
          <CartItem
            key={item.id}
            item={item}
            addToCart={addToCart}
            removeFromCart={removeFromCart}
          />
        ))
      ) : (
        <p>No items in cart</p>
      )}
        <h2>Total: ${calculateTotal(cartItems).toFixed(2)}</h2>
        <Button 
            onClick={handleCheckout}
            color="primary"
            variant="contained"
        >
            Checkout
        </Button>
    </Wrapper>
  );
};

export default Cart;

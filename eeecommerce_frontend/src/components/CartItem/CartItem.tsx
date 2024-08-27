import React from "react";
import Button from "@mui/material/Button";

// Types
import { CartItemType } from "../../types";

// Styles
import { Wrapper } from "./CartItem.styles";

type Props = {
  item: CartItemType;
  addToCart: (clickedItem: CartItemType) => void;
  removeFromCart: (id: number) => void;
};

const CartItem: React.FC<Props> = ({ item, addToCart, removeFromCart }) => {
  
  console.log("In CartItem, item: ", item)
  return (
    <Wrapper>
      <div>
        <h3>{item.title}</h3>
        <div className="information">
          <p>Original Price: ${item.price / 100}</p>
          <p>Discount: {item.discount * 100}%</p>
          <p>
            New Price: ${((item.price * (1 - item.discount)) / 100).toFixed(2)}
          </p>
          <p>
            Total: $
            {(
              (item.amount *
                Number((item.price * (1 - item.discount)).toFixed(2))) /
              100
            ).toFixed(2)}
          </p>
        </div>
        <div className="buttons">
          <Button
            size="small"
            disableElevation
            variant="contained"
            onClick={() => removeFromCart(item.id)}>
            -
          </Button>
          <p>{item.amount}</p>
          <Button
            size="small"
            disableElevation
            variant="contained"
            onClick={() => addToCart(item)}>
            +
          </Button>
        </div>
      </div>
      <img src={item.image} alt={item.title} />
    </Wrapper>
  );
};
export default CartItem;

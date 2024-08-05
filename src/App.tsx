import { useState } from "react";
import { useQuery } from "react-query";
// Components
import Item from "./Item/Item";
import Drawer from "@mui/material/Drawer";
import { LinearProgress } from "@mui/material";
import Grid from "@mui/material/Grid";
import AddShoppingCartIcon from "@mui/icons-material/AddShoppingCart";
import Badge from "@mui/material/Badge";
import Cart from "./Cart/Cart";
import { OrderItemType } from "./types";
import { getOrderItems } from "./api";


// Styles
import { Wrapper, StyledButton } from "./App.styles";

// Types
export type CartItemType = {
  id: number;
  category: string;
  description: string;
  image: string;
  price: number;
  title: string;
  amount: number;
};

const getProducts = async (): Promise<CartItemType[]> => {
  // inner await is for the api call, outer await is for the conversion to json
  const result = await (
    await fetch("https://fakestoreapi.com/products")
  ).json();
  return result;
};

function App() {
  const [cartOpen, setCartOpen] = useState(false);
  const [cartItems, setCartItems] = useState([] as CartItemType[]);
  const { data, isLoading, error } = useQuery<CartItemType[]>(
    "products",
    getProducts
  );

  console.log(data);
  const getTotalItems = (items: CartItemType[]) =>
    // iterates through cart and adds amount to get total amount of items, default value set to 0
    items.reduce((accumulator: number, items) => accumulator + items.amount, 0);

  const handleAddToCart = (clickedItem: CartItemType) => {
    // can use previous state as arg
    setCartItems((prev) => {
      // is item already in the cart?
      const isItemInCart = prev.find((item) => item.id === clickedItem.id);

      // if yes, simply increase the item's amount by 1
      if (isItemInCart) {
        return prev.map((item) =>
          item.id === clickedItem.id
            ? { ...item, amount: item.amount + 1 }
            : item
        );
      }

      // first time adding item to the cart
      return [...prev, { ...clickedItem, amount: 1 }];
    });
  };

  const handleRemoveFromCart = (id: number) => {
    setCartItems(
      (prev) => (
        prev.reduce((accumulator, items) => {
          // if current item is the one we clicked on, reduce amount by 1, if already 1, don't include the item in the cart anymore
          if (items.id == id) {
            if (items.amount === 1) return accumulator;
            return [...accumulator, { ...items, amount: items.amount - 1 }];
          } else{
            return [...accumulator, items]; // if current item is not the one we clicked one, keep it in the cart as is
          }
        }, [] as CartItemType[])        
      )
    );
  };

  if (isLoading) return <LinearProgress />;
  if (error) return <div>Something went</div>;

  return (
    <>
      <Wrapper>
        {/* Cart section that displays when cartOpen is set to true */}
        <Drawer
          anchor="right"
          open={cartOpen}
          onClose={() => setCartOpen(false)}>
          <Cart
            cartItems={cartItems}
            addToCart={handleAddToCart}
            removeFromCart={handleRemoveFromCart}
          />
        </Drawer>
        {/* Cart icon that opens cart when clicked */}
        <StyledButton onClick={() => setCartOpen(true)}>
          <Badge badgeContent={getTotalItems(cartItems)} color="error">
            <AddShoppingCartIcon />
          </Badge>
        </StyledButton>
        <Grid container spacing={3}>
          {data?.map((item) => (
            <Grid item key={item.id} xs={12} sm={4}>
              <Item item={item} handleAddToCart={handleAddToCart} />
            </Grid>
          ))}
        </Grid>
      </Wrapper>
    </>
  );
}

export default App;

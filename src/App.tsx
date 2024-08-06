import { useState } from "react";
import { useQuery } from "react-query";
import Item from "./components/Item/Item";
import Drawer from "@mui/material/Drawer";
import { LinearProgress } from "@mui/material";
import Grid from "@mui/material/Grid";
import AddShoppingCartIcon from "@mui/icons-material/AddShoppingCart";
import Badge from "@mui/material/Badge";
import Cart from "./components/Cart/Cart";
import { Wrapper, StyledButton } from "./App.styles";
import { CartItemType } from "./types";
import { getOrderItems } from "./api";
import RegistrationForm from "./components/Registration/RegistrationForm.tsx";
import LoginForm from "./components/Login/LoginForm.tsx";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./pages/layout.tsx";

function App() {
  const [cartOpen, setCartOpen] = useState(false);
  const [cartItems, setCartItems] = useState([] as CartItemType[]);
  const { data, isLoading, error } = useQuery<CartItemType[]>(
    "orderItems",
    getOrderItems
  );

  const getTotalItems = (items: CartItemType[]) =>
    items.reduce((acc: number, item) => acc + item.amount, 0);

  const handleAddToCart = (clickedItem: CartItemType) => {
    setCartItems((prev) => {
      const isItemInCart = prev.find((item) => item.id === clickedItem.id);
      console.log(clickedItem);
      if (isItemInCart) {
        return prev.map((item) =>
          item.id === clickedItem.id
            ? { ...item, amount: item.amount + 1 }
            : item
        );
      }

      return [...prev, { ...clickedItem, amount: 1 }];
    });
  };

  const handleRemoveFromCart = (id: number) => {
    setCartItems((prev) =>
      prev.reduce((acc, item) => {
        if (item.id === id) {
          if (item.amount === 1) return acc;
          return [...acc, { ...item, amount: item.amount - 1 }];
        } else {
          return [...acc, item];
        }
      }, [] as CartItemType[])
    );
  };

  if (isLoading) return <LinearProgress />;
  if (error) return <div>Something went wrong</div>;

  return (  
    <Wrapper>
      <h1>EeE-Commerce</h1>
      <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index />        
          <Route path="registration" element={<RegistrationForm />} />  {/* blogs path combines with parent Route path and becomes /blogs and displays <Blogs /> */}
          <Route path="login" element={<LoginForm />} />
        </Route>
      </Routes>
    </BrowserRouter>      
      <Drawer anchor="right" open={cartOpen} onClose={() => setCartOpen(false)}>
        <Cart
          cartItems={cartItems}
          addToCart={handleAddToCart}
          removeFromCart={handleRemoveFromCart}
        />
      </Drawer>
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
  );
}

export default App;

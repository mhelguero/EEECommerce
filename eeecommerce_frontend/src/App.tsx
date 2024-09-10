import { useEffect, useState } from "react";
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
import { getProducts } from "./api";
import { Routes, Route, useNavigate } from "react-router-dom";
import Layout from "./pages/layout.tsx";
import Registration from "./pages/registration.tsx";
import Login from "./pages/login.tsx";
import Profile from "./pages/profile.tsx";
import axios from "axios";

function App() {
  const [cartOpen, setCartOpen] = useState(false);
  const [cartItems, setCartItems] = useState([] as CartItemType[]);
  const [userId, setUserId] = useState<number | null>(null); // holds logged-in user's id to pass it to other components
  const [loggedIn, setLoggedIn] = useState<Boolean>(false); // will be set to true in LoginForm.tsx once logged in

  // route to home if user is logged in
  const navigate = useNavigate();

  useEffect(() => {
    if (userId && loggedIn) navigate("/");
    setLoggedIn(false);
    fetchCartItems();
  }, [userId, navigate]);

  const { data, isLoading, error } = useQuery<CartItemType[]>(
    "Products",
    getProducts
  );

  const getTotalItems = (items: CartItemType[]) =>
    items.reduce((acc: number, item) => acc + item.amount, 0);

  const sortByTitle = (items: CartItemType[]) => {
    return items.sort((a, b) => a.title.localeCompare(b.title));
  };

  const fetchCartItems = async () => {
    const url = "http://18.118.75.83:8080/cart";

    try {
      const response = await axios({
        url: url,
        method: "get",
        headers: {
          userId: userId,
          userType: "CUSTOMER",
        },
      });

      console.log("Fetched cart items:", response.data);

      // Map the fetched data to match the CartItemType
      const transformedData = response.data.map((item: any) => ({
        id: item.product.product_id,
        category: item.product.category,
        description: item.product.description,
        image: item.product.image,
        price: item.product.price,
        title: item.product.name,
        amount: item.count,
        discount: item.product.discount,
      }));

      setCartItems(sortByTitle(transformedData));
    } catch (error) {
      console.error("Error fetching cart items:", error);
    }
  };

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

  const handleAddToCartWithRequest = async (item: CartItemType) => {
    const url = "http://18.118.75.83:8080/cart";
    const params = new URLSearchParams();
    params.append("productId", item.id.toString());
    params.append("quantity", "1");

    const headers = {
      userId: userId, // Replace with actual userId if needed
      userType: "CUSTOMER",
    };

    try {
      await axios.post(url, params, { headers });
      console.log("Item added to cart");
      fetchCartItems(); // Fetch the updated cart items
      handleAddToCart(item);
    } catch (error) {
      console.error("Error adding item to cart:", error);
    }
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

  const handleRemoveFromCartWithRequest = async (id: number) => {
    const url = "http://18.118.75.83:8080/cart";
    const params = new URLSearchParams();
    params.append("productId", id.toString());
    params.append("quantity", "-1");

    const headers = {
      userId: userId,
      userType: "CUSTOMER",
    };

    try {
      await axios.post(url, params, { headers });
      console.log("Item removed from cart with userId = " + userId);
      fetchCartItems(); // Fetch the updated cart items
      handleRemoveFromCart(id);
    } catch (error) {
      console.error("Error removing item from cart:", error);
    }
  };

  if (isLoading) return <LinearProgress />;
  if (error) return <div>Something went wrong</div>;
  console.log("User id in App.tsx:", userId);
  return (
    <Wrapper>
      <h1>EeE-Commerce</h1>
      <Routes>
        <Route path="/" element={<Layout userId={userId} />}>
          <Route index />
          {/* "registration" path combines with parent "/" Route path and becomes "/registration" and displays <Registration /> */}
          <Route path="profile" element={<Profile userId={userId} />} />
          <Route path="registration" element={<Registration />} />
          <Route
            path="login"
            element={
              <Login
                setUserId={(id) => {
                  setUserId(id);
                  setLoggedIn(true);
                }}
              />
            }
          />
        </Route>
      </Routes>

      {/* If userId exists(user logged in), then show products for sale. If not, only show nav links from <Layout /> above */}
      {userId ? (
        <>
          {/* Cart Display*/}
          <Drawer
            anchor="right"
            open={cartOpen}
            onClose={() => setCartOpen(false)}
            sx={{
              "& .MuiPaper-root": { backgroundColor: "black", color: "white" },
            }}>
            <Cart
              addToCart={handleAddToCartWithRequest}
              removeFromCart={handleRemoveFromCartWithRequest}
              fetchCartItems={fetchCartItems}
              cartItems={cartItems}
              userId={userId}
            />
          </Drawer>
          <StyledButton onClick={() => setCartOpen(true)}>
            <Badge badgeContent={getTotalItems(cartItems)} color="error">
              <AddShoppingCartIcon />
            </Badge>
          </StyledButton>

          {/* Product Display */}
          <Grid container spacing={3}>
            {data?.map((item) => (
              <Grid item key={item.id} xs={12} sm={4}>
                <Item
                  item={item}
                  handleAddToCart={handleAddToCart}
                  userId={userId}
                />
              </Grid>
            ))}
          </Grid>
        </>
      ) : (
        <></>
      )}
    </Wrapper>
  );
}

export default App;

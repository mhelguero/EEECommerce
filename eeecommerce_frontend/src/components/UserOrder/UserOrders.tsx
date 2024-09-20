import axios from "axios";
import { Order } from "../../types";
import { Card, CardContent, Typography, List, ListItem, ListItemText, Divider, Grid } from "@mui/material";
import { useEffect, useState } from "react";

interface Props {
  userId: number | null;
}

const UserOrders: React.FC<Props> = ({ userId }: { userId: number | null }) => {
  // this will hold the retrieved User data

  const [userOrders, setUserOrders] = useState<Order[]>([]);

  // runs as soon as this Route is visited
  useEffect(() => {
    // arrow function to retrieve user info
    const getUserOrders = async (userId: number) => {
      try {
        console.log("user id in UserOrders.tsx: ", userId);
        const response = await axios.get(
          `http://3.144.166.99:8080/orders/user/${userId}`
        );

        // GET reseponse.data has same structure as UserType, so can assign directly to userOrders via setUserOrders()
        setUserOrders(response.data);

        const userOrders = response.data;
        console.log("userOrders retrieved: ", userOrders);
        userOrders.map(async (order: { orderId: number }) => {
          const orderItems = await getOrderItems(order.orderId);

          orderItems.map((item: { count: number, orderId: number, orderItemId: number, product_id: number }) => {
            console.log(`Order item details:
              Count: ${item.count},
              Order ID: ${item.orderId},
              Order Item ID: ${item.orderItemId},
              Product ID: ${item.product_id}`);
          });
        });
      } catch (error) {
        console.error("Error during request:", error);
      }
    };

    // executes the function to retrieve and set User info
    if (userId) {
      getUserOrders(userId);
    }
  }, [userId]);

  
  return (
    <>
      <Typography variant="h4" gutterBottom>
        Profile
      </Typography>
      <Typography variant="h6">Orders:</Typography>
      <Grid container spacing={2}>
        {userOrders.map((order) => (
          <Grid item xs={12} key={order.orderId}>
            <Card>
              <CardContent>
                <Typography variant="h6">
                  Order ID: {order.orderId}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                  Order Date: {order.date}
                </Typography>
                <Divider style={{ margin: "10px 0" }} />

                {/* Display order items for each order */}
                <OrderItems orderId={order.orderId} />
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </>
  );
};

export default UserOrders;

import { List, ListItem, ListItemText, Typography } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { OrderItem } from "../../types";


interface Props {
    orderId: number | null;
  }
  
const OrderItems: React.FC<Props> = ({ orderId }) => {
    const [orderItems, setOrderItems] = useState<OrderItem[]>([]);

    useEffect(() => {
        const fetchOrderItems = async () => {
        const items = await getOrderItems(orderId);
        setOrderItems(items);
        };

        fetchOrderItems();
    }, [orderId]);


    const getOrderItems = async (orderId: number|null): Promise<OrderItem[]> => {
        try {
            console.log("order id: ", orderId);
            const response = await axios.get(
            `http://3.144.166.99:8080/orderItems/order/${orderId}`
            );

            // GET reseponse.data has same structure as UserType, so can assign directly to userOrders via setUserOrders()
            console.log("Order Item data: ", response.data);
            return response.data;
        } catch (error) {
            console.error("Error during request:", error);
            return [];
        }
        };
        
    return (
        <List>
        {orderItems.map((item) => (
            <ListItem key={item.orderItemId}>
            <ListItemText
                //primary={`Product ID: ${item.product_id}`}
                secondary={
                <>
                    <Typography component="span" variant="body2">
                    Quantity: {item.count}
                    </Typography>
                    <br />
                    Order Item ID: {item.orderItemId}
                </>
                }
            />
            </ListItem>
        ))}
        {orderItems.length === 0 && (
            <Typography variant="body2" color="textSecondary">
            No items for this order.
            </Typography>
        )}
        </List>
    );
};
export default OrderItems;

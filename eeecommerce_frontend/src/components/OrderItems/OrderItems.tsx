import { List, ListItem, ListItemText, Typography } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { OrderItem, Product } from "../../types";


interface Props {
    orderId: number | null;
  }
  
const OrderItems: React.FC<Props> = ({ orderId }) => {
    const [orderItems, setOrderItems] = useState<OrderItem[]>([]);

    useEffect(() => {
        const fetchOrderItems = async () => {
        const items = await getOrderItems(orderId);
        console.log("OrderItems.tsx orderItems: ", items);
        setOrderItems(items);
        };

        fetchOrderItems();        
    }, [orderId]);

    const getOrderItems = async (orderId: number|null): Promise<OrderItem[]> => {
        try {
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
    const getProducts = async (item: OrderItem|null): Promise<Product|String> => {
        try {
            console.log("orderitem id: ", item?.orderItemId);
            const response = await axios.get(
            `http://3.144.166.99:8080/products/${item?.product.product_id}`
            );
    
            // GET reseponse.data has same structure as UserType, so can assign directly to userOrders via setUserOrders()
            console.log("Product data for order item "+item?.orderItemId+": ", response.data);
            return response.data;
        } catch (error) {
            console.error("Error during request:", error);
            return "Error retrieving product";
        }
    }
        
    return (
        <List>
        {orderItems.map((item) => (
            <ListItem key={item.orderItemId}>
            <ListItemText
            // TODO: use getProducts to retrieve Products via item.product_id to be able to display name
                primary={`${item.product.name}`}
                secondary={
                <>
                    <Typography component="span" variant="body2">
                    Quantity: {item.count}
                    </Typography>
                    <br />
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

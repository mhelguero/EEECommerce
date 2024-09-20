import { List, ListItem, ListItemText, Typography } from "@mui/material";
import axios from "axios";
import { useEffect, useState } from "react";
import { OrderItem } from "../../types";


interface Props {
    orderId: number | null;
    getOrderItems: (orderId: number | null) => Promise<OrderItem[]>;
  }
  
const OrderItems: React.FC<Props> = ({ orderId, getOrderItems }) => {
    const [orderItems, setOrderItems] = useState<OrderItem[]>([]);

    useEffect(() => {
        const fetchOrderItems = async () => {
        const items = await getOrderItems(orderId);
        setOrderItems(items);
        };

        fetchOrderItems();
    }, [orderId]);


    
        
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

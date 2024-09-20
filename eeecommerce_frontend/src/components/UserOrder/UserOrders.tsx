import axios from "axios";
import { Order } from "../../types";
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
        
      } catch (error) {
        console.error("Error during request:", error);
      }
    };

    // executes the function to retrieve and set User info
    if (userId) {
      getUserOrders(userId);
    }
  }, [userId]);


  const getOrderItems = async(orderId: number) =>{
    try {
      console.log("order id: ", orderId);
      const response = await axios.get(
        `http://3.144.166.99:8080/orderItems/order/${orderId}`
      );
      // response is
      /*
        [
          {
              "orderItemId": 1,
              "orderId": 1,
              "product_id": 1,
              "count": 1
          }
        ]
      */

      // GET reseponse.data has same structure as UserType, so can assign directly to userOrders via setUserOrders()
      console.log("Order Item data: ", response.data);
    } catch (error) {
      console.error("Error during request:", error);
    }
  }
  return (
    <>
      {/* display the retrieved user data as the content of this component*/}
      <h2>Profile</h2>
      <ul>

         <li>Orders:</li>
         {userOrders.map((order) => (
           <li key={order.orderId}>
             Order ID: {order.orderId}
             Time: {order.time}
           </li>
         ))}    
       </ul>
    </>
  );
};

export default UserOrders;

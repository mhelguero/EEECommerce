import axios from "axios";
import { Order, UserType } from "../../types";
import { useEffect, useState } from "react";

interface Props {
  userId: number;
}

const UserOrders: React.FC<Props> = ({ userId }: { userId: number }) => {
  // this will hold the retrieved User data
  const [userOrders, setUserOrders] = useState<Order | null>(null);

  // runs as soon as this Route is visited
  useEffect(() => {
    // arrow function to retrieve user info
    const getUserOrders = async (userId: number) => {
      try {
        console.log("user id in UserOrders.tsx: ", userId);
        const response = await axios.get(
          `http://localhost:8080/users/${userId}`
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

  return (
    <>
      {/* display the retrieved user data as the content of this component*/}
      <h2>Profile</h2>
      <ul>
        <li>Orders:</li>
        <li>{userOrders?.orderId}</li>
        <li>{userOrders?.date}</li>
      </ul>
    </>
  );
};

export default UserOrders;

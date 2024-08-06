import axios from "axios";
import { UserType } from "../../types";
import { useEffect, useState } from "react";

interface Props {
  userId: number;
}

const UserInfo: React.FC<Props> = ({ userId }: { userId: number }) => {
  // this will hold the retrieved User data
  const [userInfo, setUserInfo] = useState<UserType | null>(null);

  // runs as soon as this Route is visited
  useEffect(() => {
    // arrow function to retrieve user info
    const getUserInfo = async (userId: number) => {
      try {
        console.log("user id in UserInfo.tsx: ", userId);
        const response = await axios.get(
          `http://localhost:8080/users/${userId}`
        );
        setUserInfo(response.data);

        const userInfo = response.data;
        console.log("userInfo retrieved: ", userInfo);
      } catch (error) {
        console.error("Error during request:", error);
      }
    };

    // executes the function to retrieve and set User info
    if (userId) {
      getUserInfo(userId);
    }
  }, [userId]);

  return (
    <>
      {/* display the retrieved user data as the content of this component*/}
      <h2>Profile</h2>
      <ul>
        <li>
          {userInfo?.firstName} {userInfo?.lastName}
        </li>
        <li>{userInfo?.userType.toLowerCase()}</li>
        <li>{userInfo?.address}</li>
        <li>{userInfo?.email}</li>
      </ul>
    </>
  );
};

export default UserInfo;

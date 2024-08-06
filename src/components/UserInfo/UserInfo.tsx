import axios from "axios";
import { UserType } from "../../types";
import { useEffect, useState } from "react";

interface Props {
  userId: number;
}

const UserInfo: React.FC<Props> = ({ userId }: { userId: number }) => {
  // this will hold the retrieved User Data
  const [userInfo, setUserInfo] = useState<UserType | null>(null);

  // runs as soon as this Route is visited
  useEffect(() => {
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

    // executes the function to retrieve and set User Info
    if (userId) {
      getUserInfo(userId);
    }
  }, [userId]);

  return (
    <>
      {/* display the retrieved user data as the content of this component*/}
      {userInfo?.firstName}
    </>
  );
};

export default UserInfo;

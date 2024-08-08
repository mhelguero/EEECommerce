import UserInfo from "../UserInfo/UserInfo";
import UserOrders from "../UserOrder/UserOrders";

interface Props{
  userId: number;
}

const UserProfile: React.FC<Props> = ({ userId }: { userId: number}) => {
  return <>
    <UserInfo userId={userId} />    
    <UserOrders userId={userId} />
  </>;
};

export default UserProfile;
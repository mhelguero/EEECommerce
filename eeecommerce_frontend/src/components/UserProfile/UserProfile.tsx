import UserInfo from "../UserInfo/UserInfo";
import UserOrders from "../UserOrder/UserOrders";

interface Props{
  userId: number | null;
}

const UserProfile: React.FC<Props> = ({ userId }: { userId: number | null}) => {
  return <>

    <UserInfo userId={userId} />

    <UserOrders userId={userId} />
  </>;
};

export default UserProfile;
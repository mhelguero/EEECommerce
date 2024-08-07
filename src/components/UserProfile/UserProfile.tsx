import UserInfo from "../UserInfo/UserInfo";

interface Props{
  userId: number;
}

const UserProfile: React.FC<Props> = ({ userId }: { userId: number}) => {
  return <>
    <UserInfo userId={userId} />
  </>;
};

export default UserProfile;
interface Props{
  userId: number;
}

const UserProfile: React.FC<Props> = ({ userId }: { userId: number}) => {
  return <>
    {userId}
  </>;
};

export default UserProfile;
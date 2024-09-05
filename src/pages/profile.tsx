import UserProfile from "../components/UserProfile/UserProfile";

export default function Profile({ userId }: { userId: number}) {
  return <UserProfile userId={userId}/>;
}

import UserProfile from "../components/UserProfile/UserProfile";

export default function Profile({ userId }: { userId: number | null }) {
  return <UserProfile userId={userId} />;
}

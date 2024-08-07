import LoginForm from "../components/Login/LoginForm";

export default function Login({setUserId}: {setUserId: (userId: number) => void}) {
  
  return <LoginForm setUserId={setUserId}/>;
}

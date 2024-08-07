import { Link, Outlet } from "react-router-dom";

const Layout = ({ userId }: { userId: number | null }) => {
  return (
    <>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          {userId ? (
            <li>
              <Link to="profile">Profile</Link>
            </li>
          ) : (
            <li>
              <Link to="/registration">Register</Link>
            </li>
          )}
          <li>
            <Link to="login">Login</Link>
          </li>
        </ul>
      </nav>

      <Outlet />
    </>
  );
};

export default Layout;

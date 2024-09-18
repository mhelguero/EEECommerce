import axios from "axios";
import React, { useState } from "react";

interface UserLogin {
  email: string;
  password: string;
}

interface Props {
  setUserId: (userId: number) => void;
}

const LoginForm: React.FC<Props> = ({ setUserId }) => {
  // This will hold user input into the Login form
  const [credentials, setCredentials] = useState<UserLogin>({
    email: "",
    password: "",
  });

  const [status, setStatus] = useState<number>(0);
  /**
   * Stores user input for email and password inside credentials.email and credentials.password to pass credentials into handleSubmit().
   *
   * @param event
   */
  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setCredentials({ ...credentials, [event.target.name]: event.target.value });
  };

  /**
   * Uses login form input to execute the postLogin method in AuthController and retrieve the logged-in user's userId and userType
   * to use them in creating Cart and Order entries.
   *
   * @param event
   */
  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    // credentials is correct
    try {
      const response = await axios.post(
        `http://3.144.166.99:8080/auth?email=${credentials.email}&password=${credentials.password}`,
        credentials
      );

      // retrieve logged-in user's userId and userType from response headers
      const userId = response.headers["userId"];
      console.log("LoginForm.tsx response.headers[\"userid\"]: ", userId);
      setUserId(userId);

      const userType = response.headers["usertype"];
      setStatus(response.status);

      console.log("LoginForm.tsx User ID:", userId);
      console.log("LoginForm.tsx User Type:", userType);
    } catch (error) {
      setStatus(500);
      console.error("Error during request:", error);
    }
  };

  return (
    <>
      <h3>Login</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={credentials.email}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={credentials.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {status === 0 ? (
        ""
      ) : (
        <p>{status >= 400 ? "Failed to Login" : "Login Successful"}</p>
      )}

      <br />
      <br />
    </>
  );
};

export default LoginForm;

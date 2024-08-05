import React, { useState } from "react";

interface UserLogin {
  email: string;
  password: string;
}

const LoginForm: React.FC = () => {
  // This will hold user input into the Login form
  const [credentials, setCredentials] = useState<UserLogin>({
    email: "",
    password: "",
  });

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

    try {
      const response = await fetch(
        `http://localhost:8080/auth?email=${credentials.email}&password=${credentials.password}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(credentials),
        }
      );

      if (response.ok) {
        // retrieve logged-in user's userId and userType from response headers
        const userId = response.headers.get("userId");
        const userType = response.headers.get("userType");

        console.log("User ID:", userId);
        console.log("User Type:", userType);
      } else {
        console.error("Request failed with status:", response.status);
      }
    } catch (error) {
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
      <br />
      <br />
    </>
  );
};

export default LoginForm;

import React, { useState } from "react";
import axios from "axios";

interface User {
  firstName: string;
  lastName: string;
  address: string;
  email: string;
  password: string;
  userType: "CUSTOMER" | "EMPLOYEE";
}

const RegistrationForm: React.FC = () => {
  const [user, setUser] = useState<User>({
    firstName: "",
    lastName: "",
    address: "",
    email: "",
    password: "",
    userType: "CUSTOMER",
  });

  const [status, setStatus] = useState<number>(0);

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {    
    setUser({ ...user, [event.target.name]: event.target.value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      const response = await axios.post("http://localhost:8080/users", user);
      console.log("User registered:", response.data);
      setStatus(response.status);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <h3>Register</h3>
      <form id="registration" onSubmit={handleSubmit}>
        <div>
          <label>First Name:</label>
          <input
            type="text"
            name="firstName"
            value={user.firstName}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Last Name:</label>
          <input
            type="text"
            name="lastName"
            value={user.lastName}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Address:</label>
          <input
            type="text"
            name="address"
            value={user.address}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={user.password}
            onChange={handleChange}
            required
          />
        </div>        
        <button type="submit">
          Register
        </button>
      </form>
      {status === 0 ? (
        ""
      ) : (
        <p>{status >= 400 ? "Failed to Register" : "Registration Successful"}</p>
      )}      <br />
      <br />
    </>
  );
};

export default RegistrationForm;

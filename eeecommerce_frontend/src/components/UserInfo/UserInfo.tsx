import axios from "axios";
import {UserType} from "../../types";
import React, {useEffect, useState} from "react";

interface Props {
    userId: number | null;
}

const UserInfo: React.FC<Props> = ({userId}: { userId: number | null }) => {

    // this will hold the retrieved User data
    const [user, setUserInfo] = useState<UserType | null>(null);
    // arrow function to retrieve user info
    const getUserInfo = async (userId: number) => {
        try {
            console.log("user id in UserInfo.tsx: ", userId);
            const response = await axios.get(
                `http://3.144.166.99:8080/users/${userId}`
            );
            setUserInfo(response.data);

            const userInfo = response.data;
            console.log("userInfo retrieved: ", userInfo);
        } catch (error) {
            console.error("Error during request:", error);
        }
    };

    const handleChange = (
        event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        event.preventDefault();
        setUserInfo({ ...user, [event.target.name]: event.target.value } as UserType);
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        try {
            const response = await axios({
                url: "http://3.144.166.99:8080/users",
                method: 'put',
                data: user,
            });
            console.log("User updated:", response.data);
        } catch (error) {
            console.error(error);
        }
    };

    // runs as soon as this Route is visited
    useEffect(() => {
        // executes the function to retrieve and set User info
        if (userId) {
            setUserInfo(getUserInfo(userId) as unknown as UserType);
        }
    }, []);

    return (
        <>
            {/* display the retrieved user data as the content of this component*/}
            <h2>Profile</h2>
            <form id="registration" onSubmit={handleSubmit}>
                <div>
                    <label>First Name:</label>
                    <input
                        type="text"
                        name="firstName"
                        value={user?.firstName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input
                        type="text"
                        name="lastName"
                        value={user?.lastName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Address:</label>
                    <input
                        type="text"
                        name="address"
                        value={user?.address}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        name="email"
                        value={user?.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        name="password"
                        value={user?.password}
                        onChange={handleChange}
                        required
                    />
                </div>
                <button type="submit">
                    Update Info
                </button>
            </form>
            <br />
            <br />

        </>
    );
};

export default UserInfo;

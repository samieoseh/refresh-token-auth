import { useNavigate } from "react-router-dom";
import useAuth from "./hooks/useAuth";
import { useEffect, useState } from "react";
import useRefreshToken from "./hooks/useRefreshToken";
import useAxiosPrivate from "./hooks/useAxiosPrivate";
import axios from "./api/axios";

export default function App() {
  const [users, setUsers] = useState([]);
  const [msg, setMsg] = useState(null);
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const { refresh } = useRefreshToken();
  const axiosPrivate = useAxiosPrivate();

  useEffect(() => {
    const fetchData = async () => {
      if (!isAuthenticated) {
        navigate("/login");
        return; // Exit early if not authenticated
      }

      try {
        const responseUsers = await axiosPrivate.get("/api/auth/users");
        const dataUsers = await responseUsers.data;
        setUsers(dataUsers);

        const responseHello = await axiosPrivate.get("/api/auth/hello");
        const dataHello = await responseHello.data;
        setMsg(dataHello);
        console.log(dataHello);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      }
    };

    fetchData(); // Call the async function
  }, [isAuthenticated, navigate, axiosPrivate]); // Add dependencies

  return (
    <div>
      {users.map((user) => (
        <div key={user.id}>
          <p>{user.username}</p>
          <p>
            {user.firstName} {user.lastName}
          </p>
        </div>
      ))}
      {JSON.stringify(msg)}
      <button onClick={async () => await refresh()}>
        Get new access token
      </button>
    </div>
  );
}

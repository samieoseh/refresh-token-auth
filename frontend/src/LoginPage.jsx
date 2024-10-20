import useAuth from "./hooks/useAuth";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
    const { login } = useAuth()
    const navigate = useNavigate()
  return (
    <form
      style={{
        display: "flex",
        flexDirection: "column",
        width: "200px",
        margin: "auto",
        marginTop: "100px",
        gap: "10px",
          }}
          onSubmit={async (e) => {
              e.preventDefault()
              const formData = new FormData(e.target)
              console.log(formData.get('username'), formData.get('password'))
              const data = await login({ username: formData.get("username"), password: formData.get("password") })
              console.log({data})
              if (data.accessToken) {
                  navigate('/') 
              }
          }}
    >
      <label>Username</label>
      <input
        type="text"
        name="username"
        style={{
          height: "20px",
        }}
              value="admin"
      />
      <label>Password</label>
      <input
        type="password"
        name="password"
        style={{
          height: "20px",
        }}
              value="admin"
      />
      <button
        type="submit"
        style={{
          width: "100px",
          cursor: "pointer",
        }}
      >
        Submit
      </button>
    </form>
  );
}

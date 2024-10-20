import { useState } from 'react';
import AuthContext from '../contexts/AuthContext'
import axios from '../api/axios';

export default function AuthProvider({ children }) {
    const [waitAuthCheck, setWaitAuthCheck] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(true);
    const [token, setToken] = useState(null);

    const login = async (formData) => {
        try {
            const response = await axios.post("/api/auth/login", formData, {
                withCredentials: true
            });
            const data = await response.data;
            
            if (data.accessToken) {
                axios.defaults.headers.common['Authorization'] = `Bearer ${data.accessToken}`;
                setToken(data.accessToken);
                setWaitAuthCheck(false);
                setIsAuthenticated(true);
            }
            return data;
        } catch(error) {
            console.error(error);
        }
    }

    const logout = () => {

    }

    const revokeTokens = () => {

    }


  return (
    <AuthContext.Provider
      value={{
        token,
        setToken,
        waitAuthCheck,
              isAuthenticated,
        setIsAuthenticated,
        login,
        revokeTokens,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

import { useEffect } from "react";
import { axiosPrivate } from "../api/axios";
import useRefreshToken from "./useRefreshToken";
import useAuth from "./useAuth";
import { useNavigate } from "react-router-dom";

export default function useAxiosPrivate() {
    const { token, setToken } = useAuth();
    console.log({token})
    const { refresh } = useRefreshToken();
    const navigate = useNavigate()

    useEffect(() => {
        const requestIntercept = axiosPrivate.interceptors.request.use(
            config => {
                if (!config.headers.Authorization) {
                    console.log({token}, "is found")
                    config.headers.Authorization = `Bearer ${token}`;
                }
                return config;
            }, (error) => {
                console.error({error})
                return Promise.reject(error);
            }
        )
        const responseIntercept = axiosPrivate.interceptors.response.use(
            response => response,
            async (error) => {
                console.log("An error occured" + error.res)
                const originalRequest = error.config;
                if (error.response?.status === 403 && !originalRequest.sent) {
                  originalRequest.sent = true;
                  try {
                    const { accessToken } = await refresh();
                    originalRequest.headers[
                      "Authorization"
                    ] = `Bearer ${accessToken}`;
                    setToken(accessToken);
                    return axiosPrivate(originalRequest);
                  } catch (refreshError) {
                    console.error(refreshError);
                    // Navigate to login page if refresh token expired
                    navigate("/login"); // Adjust the route to your login page
                  }
                }
                return Promise.reject(error);
            }
        )

        return () => {
            axiosPrivate.interceptors.request.eject(responseIntercept);
            axiosPrivate.interceptors.response.eject(requestIntercept);
        }
    }, [refresh, token])
    return axiosPrivate;
}

import axios from '../api/axios'

export default function useRefreshToken() {
    const refresh = async () => {
        try {
            const response = await axios.get('/api/auth/refresh', {
                headers: {
                    "Content-Type": "application/json"
                },
                withCredentials: true
            })
            const data = await response.data
            console.log(data)
            return data
        } catch (error) {
            console.error(error);
            throw new Error("Refresh token expired");
        }
    }

    return {refresh}
}

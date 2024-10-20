import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import { RouterProvider, createBrowserRouter } from "react-router-dom"
import LoginPage from './LoginPage.jsx'
import AuthProvider from './providers/AuthProvider.jsx'

const router = createBrowserRouter([
  {
    path: '/',
    element: <App />
  },
  {
    path: '/login',
    element: <LoginPage />
  }]
)

createRoot(document.getElementById("root")).render(

    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>

);

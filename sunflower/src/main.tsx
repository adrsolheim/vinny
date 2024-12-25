import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'

import HomePage from "./pages/HomePage"
import AboutPage from "./pages/AboutPage"
import NotFound from './pages/ErrorPage';
import BatchesPage from './pages/BatchesPage';

const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />,
    errorElement: <NotFound />
  },
  {
    path: '/about',
    element: <AboutPage />
  },
  {
    path: '/batches',
    element: <BatchesPage />
  },
]);
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>
);


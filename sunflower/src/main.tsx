import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import HomePage from "./pages/HomePage"
import AboutPage from "./pages/AboutPage"
import NotFound from './pages/ErrorPage';
import BatchesPage from './pages/BatchesPage';
import BatchPage from './pages/BatchPage';
import RecipesPage from './pages/RecipesPage';

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
    path: '/recipes',
    element: <RecipesPage />,
  },
  {
    path: '/batches',
    element: <BatchesPage />,
    children: [
      {
        path: 'batches/:batchId',
        element: <BatchPage />
      }
    ],
  },
]);
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>
);


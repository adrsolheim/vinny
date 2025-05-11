import { createBrowserRouter, RouterProvider } from 'react-router';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import HomePage from "./pages/HomePage"
import AboutPage from "./pages/AboutPage"
import NotFound from './pages/ErrorPage';
import BatchesPage from './pages/BatchesPage';
import BatchPage from './pages/BatchPage';
import RecipesPage from './pages/RecipesPage';
import LoginPage from './pages/Login';
import Gui from './components/Gui';
import Callback from './pages/Callback';

const router = createBrowserRouter([
  {
    element: <Gui/>,
    errorElement: <NotFound />,
    children: [
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
            path: ':batchId',
            element: <BatchPage />
          }
        ],
      },
      {
        path: '/login',
        element: <LoginPage />
      },
      {
        path: '/login/oauth2/code/sunflower',
        element: <Callback />
      }
    ]
  },
]);
createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>
);


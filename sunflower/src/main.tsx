import { createBrowserRouter, RouterProvider } from 'react-router';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import TapsPage from "./pages/TapsPage"
import AboutPage from "./pages/AboutPage"
import NotFound from './pages/ErrorPage';
import BatchesPage from './pages/BatchesPage';
import BatchPage from './pages/BatchPage';
import RecipesPage from './pages/RecipesPage';
import LoginPage from './features/Login/components/Login';
import Gui from './components/Gui';
import Callback from './features/Login/components/Callback';

const router = createBrowserRouter([
  {
    element: <Gui/>,
    errorElement: <NotFound />,
    children: [
      {
        path: '/',
        element: <TapsPage />,
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


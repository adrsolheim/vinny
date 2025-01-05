import { useEffect, useState } from 'react';
import { NavLink } from 'react-router-dom';
import Card from '../components/Card';

export default function HomePage() {
  interface Recipe {
    id: number,
    brewfatherId: string,
    name: string
  }
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const BASE_URL = 'http://127.0.0.1:8080';

  useEffect(() => {
    const fetchRecipes = async () => {
      const response = await fetch(`${BASE_URL}/api/recipes`);
      const recipes = (await response.json()) as Recipe[];
      setRecipes(recipes);
    };
    fetchRecipes();
  }, []);
    return (
      <>
        <div>
                {recipes.map((r) => <Card recipe={r}/>)}
        </div>
        <div>
            <NavLink to='/batches'>Batches</NavLink>
        </div>
      </>
    );
}
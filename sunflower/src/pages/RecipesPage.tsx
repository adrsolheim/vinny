
import { useEffect, useState } from 'react';
import RecipeCard from '../features/Recipe/components/RecipeCard';
import { Recipe } from '../features/Recipe/types';
import { fetchRecipes } from '../features/Recipe/api';

export default function RecipesPage() {
  const [recipes, setRecipes] = useState<Recipe[]>([]);

  useEffect(() => {
    const fetchAllRecipes = async () => {
      setRecipes(await fetchRecipes());
    };
    fetchAllRecipes();
  }, []);
    return (
      <main>
        {recipes.map((r) => <RecipeCard recipe={r}/>)}
      </main>
    );
}
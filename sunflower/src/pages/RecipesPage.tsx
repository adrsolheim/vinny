
import { useEffect, useState } from 'react';
import Card from '../components/RecipeCard';
import { Recipe } from '../types/recipe';
import styles from '../app.module.css';

export default function RecipesPage() {
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
      <main>
        {recipes.map((r) => <Card recipe={r}/>)}
      </main>
    );
}
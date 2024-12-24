import { useEffect, useState} from 'react';

//  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const recipes = [{'id': 1, 'name': 'Apocalyptic'}];
  const BASE_URL = 'http://127.0.0.1:8080';

//  useEffect(() => {
//    const fetchRecipes = async () => {
//      const response = await fetch(`${BASE_URL}/api/recipes`);
//      const recipes = (await response.json()) as Recipe[];
//      setRecipes(recipes);
//    };
//    fetchRecipes();
//  }, []);

  interface Recipe {
    id: number,
    brewfatherId: string,
    name: string
  }

export default function HomePage() {
    return (
        <div>
            <h1>Home</h1>
            <div className="card">
                <ul>
                {recipes.map((recipe) => {
                    return <li key={recipe.id}>{recipe.name}</li>
                })}
                </ul>
            </div>
        </div>
    );
}
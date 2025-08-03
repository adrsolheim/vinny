import { Recipe } from "./types";

const BASE_URL = 'http://127.0.0.1:8080';

export async function fetchRecipes(): Promise<Recipe[]> {
    const response = await fetch(`${BASE_URL}/api/recipes`);
    const recipes = (await response.json()) as Recipe[];
    return recipes;
}
import { useEffect } from "react";
import { getAuthUrl } from "../util/auth";

export default function Login() {
    useEffect(() => {
        redirect();
    });
}

async function redirect() {
    const authUrl = await getAuthUrl();
    window.location.href = authUrl;
}
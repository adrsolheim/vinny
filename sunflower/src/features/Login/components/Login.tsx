import { useEffect } from "react";
import { generateAuthUrl } from "../../../services/auth";

export default function Login() {
    useEffect(() => {
        redirect();
    });
}

async function redirect() {
    const authUrl = await generateAuthUrl();
    window.location.href = authUrl;
}
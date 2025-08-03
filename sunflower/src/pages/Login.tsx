import { useEffect } from "react";
import { generateAuthUrl } from "../util/auth";

export default function Login() {
    useEffect(() => {
        redirect();
    });
}

async function redirect() {
    const authUrl = await generateAuthUrl();
    window.location.href = authUrl;
}
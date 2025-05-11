import { verifyChallenge } from 'pkce-challenge';
import pkceChallenge from 'pkce-challenge';

export async function getAuthUrl(): Promise<string> {

    const { code_verifier, code_challenge } = await pkceChallenge(128);
    localStorage.setItem("pkce_verifier", code_verifier);

    const params = new URLSearchParams({
        response_type: "code",
        client_id: "sunflower",
        redirect_uri: "http://localhost:5000/login/oauth2/code/sunflower",
        code_challenge: code_challenge,
        code_challenge_method: "S256",
        scope: "api.nightfly"
    });
    console.log(params.toString())

    return `http://gatekeeper:9000/oauth2/authorize?${params.toString()}`;
}

export async function getToken(code: string) {
    
    const code_verifier = localStorage.getItem("pkce_verifier");
    if (!code_verifier) {
        throw new Error('missing code verifier');
    }

    const client_secret = import.meta.env.VITE_AUTH_CLIENT_SECRET;
    if (!client_secret) {
        throw new Error('Unable to retrieve client credentials');
    }

    const payload = new URLSearchParams({
        code: code,
        code_verifier: code_verifier,
        client_id: "sunflower",
        client_secret: client_secret,
        redirect_uri: "http://localhost:5000/login/oauth2/code/sunflower",
        grant_type: "authorization_code"

    });
    const response = await fetch("http://gatekeeper:9000/oauth2/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams(payload)
    })
    if (!response.ok) {
        throw new Error("Unable to fetch access token");
    }
    return response.json();
}
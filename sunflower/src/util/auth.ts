import pkceChallenge from 'pkce-challenge';

export async function generateAuthUrl(): Promise<string> {

    const { code_verifier, code_challenge } = await pkceChallenge(128);
    // TODO: Store in react context instead
    localStorage.setItem("pkce_verifier", code_verifier);

    const params = new URLSearchParams({
        response_type: "code",
        client_id: "sunflower",
        redirect_uri: "http://localhost:5000/login/oauth2/code/sunflower",
        code_challenge: code_challenge,
        code_challenge_method: "S256",
        scope: "api.nightfly"
    });

    return `http://gatekeeper:9000/oauth2/authorize?${params.toString()}`;
}

export async function getToken(code: string) {
    
    // TODO: Store in react context instead
    const code_verifier = localStorage.getItem("pkce_verifier");
    if (!code_verifier) {
        throw new Error('missing code verifier');
    }

    const payload = new URLSearchParams({
        code: code,
        code_verifier: code_verifier,
        client_id: "sunflower",
        redirect_uri: "http://localhost:5000/login/oauth2/code/sunflower",
        grant_type: "authorization_code"

    });
    const response = await fetch("http://gatekeeper:9000/oauth2/token", {
        method: "POST",
        credentials: "include",
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
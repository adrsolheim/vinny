import StandardButton from "../components/StandardButton";
import { getAuthUrl } from "../util/auth";

export default function Login() {
    return <>
        <StandardButton onClick={redirect} text="Login" />
    </>;
}

async function redirect() {
    const authUrl = await getAuthUrl();
    window.location.href = authUrl;
}
import { useEffect } from "react";
import { getToken } from "../util/auth";
import { useNavigate } from "react-router-dom";

export default function Callback() {
    const navigate = useNavigate();
    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get("code");
        if (code) {
            getToken(code)
            .then((token) => { 
                localStorage.setItem("token", token["access_token"])
                navigate("/")
            })
            .catch((error) => {
                console.error("Error fetching token: ", error);
            })
        }
    }, [navigate]);
    return <p>Logging in...</p>
}
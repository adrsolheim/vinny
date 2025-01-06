import { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function NotFound() {
    const navigate = useNavigate();

    useEffect(() => {
        setTimeout(() => {
            navigate('/');
        }, 1500);
    });
    return (
    <div className="flex flex-col gap-2">
        <h3>404 Not Found</h3>
        <br />
        <h4>Redirecting back..</h4>
        <Link to="/">Back to home</Link>
    </div>
    );
};
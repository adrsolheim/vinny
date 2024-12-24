import { Link } from "react-router-dom";

export default function NotFound() {
    return (
    <div className="flex flex-col gap-2">
        404 Not Found

        <Link to="/">Back to home</Link>
    </div>
    );
};
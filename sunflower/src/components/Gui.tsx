import { useState } from "react";
import { Outlet } from "react-router-dom";
import Header from "./Header";
import Sidebar from "./Sidebar";

export default function Gui() {
    const [active, setActive] = useState<boolean>(false);
    return (
        <>
            <Header sidebarActive={active} setSidebarActive={setActive}/>
            <Sidebar active={active} />
            <Outlet />
        </>
    );
}
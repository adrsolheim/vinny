import { createPortal } from "react-dom";
import { ModalProps } from "../types";
import ModalContent from "./ModalContent";

export default function TapModal(props: Readonly<ModalProps>) {
    const open = props.open;
    const batchUnits = props.batchUnits;
    return (
        <>       
        {open && createPortal(
            <ModalContent 
                open={props.open} 
                setOpen={props.setOpen} 
                batchUnits={batchUnits} 
                handleUpdateTap={props.handleUpdateTap}
            />,
            document.body
        )}
        </>   
    );
}

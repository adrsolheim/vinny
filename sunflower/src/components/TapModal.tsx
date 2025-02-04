import { useState } from "react";
import { createPortal } from "react-dom";
import styles from '../app.module.css';
import { BaselineAddCircleOutline } from '../assets/BaselineAddCircleOutline';

export default function TapModal(props: ModalProps) {
    const open = props.open;
    return (
        <>       
        {open && createPortal(
            <ModalContent open={props.open} setOpen={props.setOpen}/>,
            document.body
        )}
        </>   
    );
}

function ModalContent (props: ModalProps) {
    return (
        <div className={styles.modal}>
            <div className={styles.modalcontent}>
                <p>This is a modal</p>
                <CardButton icon={<BaselineAddCircleOutline color='white' />} {...props}/>
            </div>
        </div>
    );
}

interface ModalProps {
  open: boolean;
  setOpen: Function;
}

interface CardButtonProps {
  // this should be React.ReactElement?
  icon: JSX.Element;
  open: boolean;
  setOpen: Function;
  children?: React.ReactNode;
}
function CardButton(props : CardButtonProps) {
  const setOpen = props.setOpen;
  return (
    <li className={styles.cardbutton}>
      <a className={styles.iconbutton} href="#" onClick={() => setOpen(false)}>
        {props.icon}
      </a>
    </li>
  );
}
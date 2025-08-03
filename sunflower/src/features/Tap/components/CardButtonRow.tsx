
export default function CardButtonRow(props : Readonly<CardButtonRowProps>) {
  return (
    <nav>    
      <ul>
        {props.children}
      </ul>
    </nav>

  );
}

export interface CardButtonRowProps {
  // this should be React.ReactElement?
  children?: React.ReactNode;
}
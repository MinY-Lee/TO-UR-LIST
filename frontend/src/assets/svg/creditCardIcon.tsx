interface PropType {
    width?: number;
    className?: string;
    color?: string;
}

export default function CreditCardIcon(props: PropType) {
    return (
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
             className={`${props.className} w-6`}>
            <path fill-rule="evenodd" clip-rule="evenodd"
                  d="M19 6.6H5C4.77909 6.6 4.6 6.77909 4.6 7V8.4H19.4V7C19.4 6.77909 19.2209 6.6 19 6.6ZM4.6 17V10H19.4V17C19.4 17.2209 19.2209 17.4 19 17.4H5C4.77909 17.4 4.6 17.2209 4.6 17ZM5 5C3.89543 5 3 5.89543 3 7V17C3 18.1046 3.89543 19 5 19H19C20.1046 19 21 18.1046 21 17V7C21 5.89543 20.1046 5 19 5H5ZM7 13C6.44772 13 6 13.4477 6 14C6 14.5523 6.44772 15 7 15H9C9.55228 15 10 14.5523 10 14C10 13.4477 9.55228 13 9 13H7Z"
                  fill={props.color ? props.color : "#333333"}/>
        </svg>


    );
}

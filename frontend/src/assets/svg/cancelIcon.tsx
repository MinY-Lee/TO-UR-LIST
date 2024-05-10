interface PropType {
    width?: number;
    className?: string;
}
export default function calcelIcon(props: PropType) {
    return (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={props.width ? props.width : 3}
            stroke="currentColor"
            className={`${props.className} w-3 h-3`}
        >
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12" />
        </svg>
    );
}

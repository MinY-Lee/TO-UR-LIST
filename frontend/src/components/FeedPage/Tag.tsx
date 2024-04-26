interface PropType {
    tagName: string;
    tagType: string;
}

export default function Tag(props: PropType) {
    return (
        <div
            className={`text-[4vw] weight-text-semibold px-[1vw] mr-[0.5vw] flex justify-center items-center rounded-full ${
                props.tagType === 'theme' ? 'bg-[#FFD4D4]' : 'color-bg-blue-4'
            }`}
        >
            {`#${props.tagName}`}
        </div>
    );
}

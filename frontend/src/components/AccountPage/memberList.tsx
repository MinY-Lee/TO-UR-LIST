import { MemberInfo, PayMember } from "../../types/types";

interface PropType {
    memberList: MemberInfo[];
    payMember: PayMember[];
    userId: string;
    handlePayMember: (userId: string) => void;
}

export default function MemberList(props: PropType) {
    return (
        <>
            {props.memberList.map((member, index) => (
                <div
                    onClick={() => props.handlePayMember(member.userId)}
                    key={index}
                    className={`flex gap-2 ${
                        props.payMember
                            .map((member) => member.userId)
                            .includes(member.userId)
                            ? ""
                            : "grayscale"
                    } items-center`}
                >
                    <div>
                        {props.payMember
                            .map((member) => member.userId)
                            .includes(member.userId)
                            ? "x"
                            : "+"}
                    </div>
                    <div>
                        <div className="text-white col-span-1 color-bg-blue-2 w-7 h-7 flex justify-center shadow-lg items-center rounded-full">
                            {member.userName[0]}
                        </div>
                    </div>
                    {member.userName}{" "}
                    {member.userId == props.userId ? " (나)" : ""}
                    <div></div>
                </div>
            ))}
        </>
    );
}

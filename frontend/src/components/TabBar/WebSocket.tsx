import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";
import { Cookies } from "react-cookie";

import { UserInfo, WebSockPlace } from "../../types/types";
import { useSelector } from "react-redux";

import { jwtDecode } from "jwt-decode";

interface PropType {
    tourId: string;
    setWsClient: React.Dispatch<React.SetStateAction<Client>>;
    update: (newSchedule: WebSockPlace[]) => void;
}

export default function WebSocket(props: PropType) {
    const userInfo: UserInfo = useSelector((state: any) => state.userSlice);

    const cookies = new Cookies();

    //handshake
    useEffect(() => {
        const accessToken = cookies.get("accessToken");
        const decoded: any = jwtDecode(accessToken);

        const userId = decoded.userId;
        const userName = userInfo.userName;
        const userNickname = userInfo.userNickname;

        const newClient = new Client({
            brokerURL: `${import.meta.env.VITE_REACT_WEBSOCKET_URL}`,
            connectHeaders: {
                UserId: userId,
                Username: userName,
                UserNickname: userNickname,
            },
            debug: function (str) {
                // console.log(str);
            },
            reconnectDelay: 5000,
        });

        newClient.onConnect = () => {
            //subscribe
            newClient.subscribe(`/topic/place/${props.tourId}`, (message) => {
                const msg = JSON.parse(message.body);

                switch (msg.type) {
                    case "ADD_PLACE":
                        // console.log(msg);
                        break;
                    case "UPDATE_PLACE":
                        // console.log(msg.body);
                        // console.log("updated");
                        props.update(msg.body.placeList);
                        break;
                    case "DELETE_PLACE":
                        // console.log(msg);
                        break;
                    case "UPDATE_PLACE_DATE":
                        // console.log(msg);
                        break;
                    case "ADD_ACTIVITY":
                        // console.log(msg);
                        break;
                    case "DELETE_ACTIVITY":
                        // console.log(msg);
                        break;
                }
            });
        };

        newClient.activate();

        props.setWsClient(newClient);
    }, [props.tourId]);

    return <div className="hidden"></div>;
}

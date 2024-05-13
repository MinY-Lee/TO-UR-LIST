import { Client } from '@stomp/stompjs';
import { useEffect, useState } from 'react';

export default function WebSocket() {
    const [wsClient, setWsClient] = useState<Client>();
    //handshake
    useEffect(() => {
        const newClient = new Client({
            brokerURL: `ws://localhost:8080/place`,
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
        });

        setWsClient(newClient);
    }, []);

    return <div className="w-0 h-0 hidden"></div>;
}

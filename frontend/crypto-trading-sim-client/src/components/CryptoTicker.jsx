import { useEffect, useState } from "react";

const pairs = [
    "XBT/USD", "ETH/USD", "USDT/USD", "XRP/USD", "BNB/USD", "SOL/USD",
    "USDC/USD", "TRX/USD", "XDG/USD", "ADA/USD", "AAVE/USD", "SUI/USD",
    "BCH/USD", "LINK/USD", "ICP/USD", "XLM/USD", "AVAX/USD", "TON/USD",
    "NEAR/USD", "XMR/USD"
];

export default function CryptoTicker() {
    const [prices, setPrices] = useState({});

    useEffect(() => {
        const ws = new WebSocket("wss://ws.kraken.com");

        ws.onopen = () => {
            console.log("WebSocket connected");

            ws.send(JSON.stringify({
                event: "subscribe",
                pair: pairs,
                subscription: { name: "ticker" }
            }));
        };

        ws.onmessage = (msg) => {
            try {
                const data = JSON.parse(msg.data);

                if (data.event === "subscriptionStatus") {
                    console.log(`Subscribed to ${data.pair}`);
                    return;
                }

                if (!Array.isArray(data)) return;

                const pair = data[3] || data[2];
                const ticker = data[1];
                const price = ticker?.c?.[0];

                if (pair && price) {
                    setPrices(prev => ({
                        ...prev,
                        [pair]: price
                    }));
                }
            } catch (e) {
                console.error("WebSocket message parse error:", e);
            }
        };

        ws.onerror = (err) => {
            console.error("WebSocket error", err);
        };

        ws.onclose = () => {
            console.warn("WebSocket closed");
        };

        return () => {
            if (ws.readyState === WebSocket.OPEN) {
                ws.close();
            }
        };
    }, []);

    return (
        <div>
            <h2>Live Prices</h2>
            <table>
                <thead>
                <tr>
                    <th>Pair</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {pairs.map(pair => (
                    <tr key={pair}>
                        <td>{pair}</td>
                        <td>{prices[pair] ? `$${parseFloat(prices[pair]).toFixed(2)}` : "Loading..."}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

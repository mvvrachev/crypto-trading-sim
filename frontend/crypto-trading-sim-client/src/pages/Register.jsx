import React, { useState } from 'react';
import axios from 'axios';

export default function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [msg, setMsg] = useState('');
    const [error, setError] = useState('');

    const submit = async (e) => {
        e.preventDefault();
        setMsg('');
        setError('');

        try {
            const res = await axios.post('/auth/register', { username, password });
            setMsg(res.data);
            setUsername('');
            setPassword('');
        } catch (err) {
            if (err.response) {
                setError(err.response.data);
            } else {
                setError('Network error');
            }
        }
    };

    return (
        <form onSubmit={submit}>
            <h2>Register</h2>
            {msg && <p style={{ color: 'green' }}>{msg}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <input
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
            />
            <input
                placeholder="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            <button type="submit">Register</button>
        </form>
    );
}

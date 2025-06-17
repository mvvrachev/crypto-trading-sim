import React, { useState } from 'react';
import axios from 'axios';

export default function Login({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const submit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            const res = await axios.post('/auth/login', { username, password });
            onLogin(res.data); // assuming backend returns token as plain text
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
            <h2>Login</h2>
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
            <button type="submit">Login</button>
        </form>
    );
}

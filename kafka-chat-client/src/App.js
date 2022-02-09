import './App.css';

import React, { useState } from 'react'
import { randomColor } from './utils/Common';
import SockJsClient from 'react-stomp';
import Input from './components/Input';
import LoginForm from './components/LoginForm';
import Messages from './components/Messages';
import ChatAPI from './services/ChatApi';
import UserContext from './context/UserContext';

const SOCKET_URL = 'http://localhost:8080/ws-chat/';

const App = () => {
    const [messages, setMessages] = useState([])
    const [user, setUser] = useState(null)

    let onConnected = () => {
        console.log("Connected!!")
    }

    let onMessageReceived = (msg) => {
        console.log('New Message Received!!', msg);
        setMessages(messages.concat(msg));
    }

    let onSendMessage = (msgText) => {
        ChatAPI.sendMessage(user.username, msgText).then(res => {
            console.log('Sent', res);
        }).catch(err => {
            console.log('Error Occured while sending message to api');
        })
    }

    let handleLoginSubmit = (username) => {
        console.log(username, " Logged in..");

        setUser({
            username: username,
            color: randomColor()
        })
    }

    return (
        <div className="App">
            {!!user ?
                (
                    <>
                        <SockJsClient
                            url={SOCKET_URL}
                            topics={['/topic/group']}
                            onConnect={onConnected}
                            onDisconnect={console.log("Disconnected!")}
                            onMessage={msg => onMessageReceived(msg)}
                            debug={false}
                        />
                        <UserContext.Provider value={user}>
                            <Messages
                                messages={messages}
                            />
                        </UserContext.Provider>
                        <Input onSendMessage={onSendMessage} />
                    </>
                ) :
                <LoginForm onSubmit={handleLoginSubmit} />
            }
        </div>
    )
}

export default App

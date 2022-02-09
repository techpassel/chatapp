import React from 'react'
import MessageUnit from './MessageUnit';

const Messages = ({messages}) => {
    
    return (
        <ul className="messages-list">
            {messages.map((msg, i) => <MessageUnit message={msg} key={i}/>)}
        </ul>
    )

}

export default Messages
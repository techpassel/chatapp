import React, {useContext} from 'react'
import UserContext from '../context/UserContext';

const MessageUnit = ({ message }) => {
    const currentUser = useContext(UserContext);
    const { sender, content, color } = message;
    const messageFromMe = currentUser.username === message.sender;
    const className = messageFromMe ? "Messages-message currentUser" : "Messages-message";
    return (
        <li className={className}>
            <span
                className="avatar"
                style={{ backgroundColor: color }}
            />
            <div className="Message-content">
                <div className="username">
                    {sender}
                </div>
                <div className="text">{content}</div>
            </div>
        </li>
    );
}

export default MessageUnit
import Axios from "axios";

const api = Axios.create({
    baseURL: 'http://localhost:8080/api/',
    withCredentials: false,
    headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
    }
});

const ChatAPI = {

    sendMessage: (username, text) => {
        let msg = {
            sender: username,
            content: text
        }
        
        return api.post(`send`, msg);
    }
}


export default ChatAPI;
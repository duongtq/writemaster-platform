import React, { useState } from 'react'
import ChatArea from './ChatArea';
import MessengerList from './MessengerList';

const ChatPage = () => {

  const [conversation, setConversation] = useState([]);

  const [messengerList, setMessengerList] = useState([]);

  const [receiver, setReceiver] = useState({});

  return (
    <div className='container'>
      <div className='row'>
        <div className='col-lg-9 chat-area' >
          <ChatArea
            messengerList={messengerList}
            receiver={receiver}
            setReceiver={setReceiver}
            conversation={conversation}
            setConversation={setConversation}
          />
        </div>
        <hr />
        <div className='col-lg-3 messenger-list'>
          <MessengerList
            messengerList={messengerList}
            setMessengerList={setMessengerList}
            receiver={receiver}
            setReceiver={setReceiver}
            conversation={conversation}
            setConversation={setConversation}
          />
        </div>
      </div>
    </div>
  )
}

export default ChatPage;

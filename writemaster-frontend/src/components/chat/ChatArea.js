import React from 'react';
import { useState } from 'react';
import { Input } from 'antd';
import Conversation from './Conversation';
import default_avatar from '../../assets/default_avatar.png';
import Helper from '../../utils/Helper';
const { Search } = Input;

const ChatArea = ({
  messengerList,
  receiver,
  setReceiver,
  conversation,
  setConversation
}) => {
  
  const sender = JSON.parse(localStorage.getItem('currentUser'));

  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  const sendMessage = (value) => {

    if (!value) {
      // alert('A empty mesage is no message. :)');
      return;
    }

    console.log('new message: ', value);

    const newMessage = {
      sender: {
        id: currentUser.id,
        username: currentUser.username
      },
      receiver: {
        id: receiver.id,
        username: receiver.username
      },
      content: value,
      sent_at: Helper.getDefaultDateTime()
    }

    const payload = {
      channel: 'incoming-message',
      message: newMessage,
    }

    setConversation([...conversation, {
      from: currentUser.username,
      to: receiver.username,
      content: value,
      sent_at: new Date()
    }])
  }

  return (
    <div style={{
      border: '1px solid black',
      height: '680px'
    }}>

      <div style={{
        marginTop: '0.5rem',
        marginLeft: '0.5rem',
        // marginBottom: '5px',
        // height: '50px',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center'
      }}>
        <div>
          <img
            src={receiver.avatar_url ? receiver.avatar_url : default_avatar}
            alt=""
            style={{
              width: '50px',
              height: '50px',
              borderRadius: '25px',
              padding: '0.25rem'
            }}
          />

        </div>
        <div style={{
          marginLeft: '5px',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start'
        }}>
          <span
            style={{
              fontSize: '1.5rem',
              fontWeight: 'bolder'
            }}
          >{receiver.firstName} {receiver.lastName}</span>
          <span>@{receiver.username}</span>
        </div>
      </div>

      <hr className='chat-page-hr' />

      <div style={{
        height: '515px'
      }}>
        {/* <p>Conversation here</p> */}
        <Conversation
          conversation={conversation}
          setConversation={setConversation}
        />
      </div>

      <hr />

      <div>
        {/* <p>Send message here</p> */}
        {/* <input type="text" /> */}

        <Search
          placeholder='Write a message...'
          enterButton='Send'
          allowClear
          bordered={false}
          onSearch={(value) => sendMessage(value)}
        />

      </div>
    </div>
  )
}

export default ChatArea;

import React from 'react';
import { useState, useEffect, useRef } from 'react';
import Message from './Message';

const Conversation = ({
  conversation,
  setConversation
}) => {

  // console.log('conv: ', conversation);

  console.log('re-render here :((((');

  return (
    <div id='data' style={{
      overflowY: 'scroll',
      height: '100%',
    }}>
      {
        conversation.map(ele => {
          return <Message key={ele._id} message={ele} />
        })
      }
    </div>
  );
}

export default Conversation;

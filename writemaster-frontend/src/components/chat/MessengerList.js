import React from 'react'
import Messenger from './Messenger';
import { Input, List } from 'antd';
import { useState, useEffect } from 'react';
import axios from 'axios';


const MessengerList = ({
  messengerList,
  setMessengerList,
  receiver,
  setReceiver,
  conversation,
  setConversation
}) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'))

  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {

    // FIXME: Refactor here
    axios
      .post('http://localhost:5000/messenger-list', {
        id: currentUser.id,
        username: currentUser.username
      })
      .then(res => {
        // console.log('messenger list 1: ', res.data);
        setMessengerList(res.data);
      })
      .catch(err => {
        console.log('error get messenger list: ', err);
      });
  }, []);

  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column'
    }}>
      <Input
        // className='mb-2'
        style={{
          marginBottom: '2rem'
        }}
        placeholder='Search Messenger...'
        allowClear
        onChange={(evt) => setSearchTerm(evt.target.value)}
      />
      
      <List
        style={{
          textAlign: 'center',
          // marginTop: '0.5rem'
        }}
        itemLayout='vertical'
        size='small'
        pagination={{
          pageSize: 8,
          style: {
            textAlign: 'center',
            marginTop: '0.125rem !important',
            // marginBottom: '2em',
          }
        }}

        dataSource={messengerList
          .filter(messenger => messenger.username !== currentUser.username)
          .filter(messenger => messenger.username.toLowerCase().includes(searchTerm.toLowerCase()))
          .sort((a, b) => {
          if (a.status < b.status) {
            return 1;
          }
          if (a.status > b.status) {
            return -1;
          }
          return 0;
        })}
        renderItem={item => (
          <Messenger
            messenger={item}
            receiver={receiver}
            setReceiver={setReceiver}
            conversation={conversation}
            setConversation={setConversation}
          />
        )}
      >

      </List>

    </div>
  )
}

export default MessengerList;

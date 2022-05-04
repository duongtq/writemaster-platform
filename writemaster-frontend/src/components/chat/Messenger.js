import React from 'react'
import { faCircle } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import default_avatar from '../../assets/default_avatar.png';
import axios from 'axios';

const Messenger = ({
  messenger,
  receiver,
  setReceiver,
  conversation,
  setConversation
}) => {
  const sender = JSON.parse(localStorage.getItem('currentUser'));
  // const color = messenger.status === 'offline' ? '#dddddd' : 'white';
  const color = 'FFFFFF';

  const statusIconColor = messenger.status === 'offline' ? '#F63638' : '#5AD539';
  const avatarUrl = messenger.avatar_url === null ? default_avatar : messenger.avatar_url;

  const getConversation = async () => {
    setReceiver(messenger);

    try {
      // FIXME: Refactor here
      const getMessages = await axios.post(`http://localhost:5000/conversations`,
        [
          {
            id: sender.id,
            username: sender.username
          },
          {
            id: messenger.id,
            username: messenger.username
          }
        ].sort((a, b) => {
          if (a.id > b.id) {
            return 1;
          }
          if (a.id < b.id) {
            return -1;
          }
          return 0;
        })); 
      
      console.log(' messages received: ', getMessages.data);

      setConversation(getMessages.data);
    } catch (err) {
      console.log('error get messages');
    }
  }

  return (
    <div className='messenger' style={{     
      display: 'flex',
      alignItems: 'center',
      borderRadius: '10px',
      background: color
    }}
      onClick={() => getConversation()}
    >
      <div>
        <img
          src={avatarUrl}
          alt=""
          style={{
            padding: '0.4rem',
            width: '70px',
            height: '70px',
            borderRadius: '50px',
            marginLeft: '0.5rem'
          }}
        />

      </div>
      <div style={{
        marginLeft: '0.5rem',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start'
      }}>
        <span>{messenger.username}</span>
        <span><FontAwesomeIcon icon={faCircle} color={statusIconColor} /> {messenger.status}</span>
      </div>
    </div>
  );
}

export default Messenger;

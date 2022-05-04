import React from 'react'
import { Tooltip } from 'antd';
import Helper from '../../utils/Helper';

const Message = ({ message }) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  // const isFromCurrentUser = message.from === 'username1';
  const marginLeft = message.from === currentUser.username ? 'auto' : '1rem';
  const marginRight = message.from === currentUser.username ? '1rem' : 'auto';
  const tooltipPlacement = message.from === currentUser.username ? 'left' : 'right'

  const backgroundColor = message.from === currentUser.username ? '#0183FF' : '#E5E7EB';

  const color = message.from === currentUser.username ? '#FEFFFE' : '#050404';

  // console.log('sent date correct: ', message.sent_at);

  return (
    <div className='clearfix' style={{
      // float: isFromCurrentUser ? 'right' : 'left',
      textAlign: 'left',
      // textAlign: 'right',
      padding: '0.5rem',
      // marginBottom: '0.5rem',
      width: '45%',
      // border: '1px solid black',
      // marginLeft: 'auto',
      // marginRight: '1rem',
      marginLeft: marginLeft,
      marginRight: marginRight,
      // borderRadius: '25px'
      // color: color,
      // backgroundColor: backgroundColor
    }}>
      <Tooltip
        title={`${Helper.getSentDate(message.sent_at)}`}
        // title={`${message.sent_at}`}

        placement={`${tooltipPlacement}`}
      >
        <p
          style={{
            border: '#FAFAFB',
            borderRadius: '25px',
            padding: '0.5rem',
            color: color,
            backgroundColor: backgroundColor
          }}
        >{message.content}</p>
      </Tooltip>
      {/* <span>Sent at {message.sent_at}</span> */}
    </div>
  );
}

export default Message;

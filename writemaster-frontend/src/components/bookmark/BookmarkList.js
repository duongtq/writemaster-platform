import React from 'react'
import { useEffect, useState } from 'react';
import axios from 'axios';
import { List } from 'antd';
import Bookmark from './Bookmark';

const BookmarkList = () => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  const [bookmarks, setBookmarks] = useState([]);

  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    console.log('in BookmarkList useEffect');
    axios
      .get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/bookmarks/${currentUser.id}`)
      .then(res => {
        console.log('bookmarked posts: ', res.data);
        setBookmarks(res.data.sort((a, b) => {
          if (a.createdDate > b.createdDate) {
            return -1;
          }
          if (a.createdDate < b.createdDate) {
            return 1;
          }
          return 0;
        }));
      })
      .catch(err => {
        console.log('Error fetching bookmarked posts: ', err);
      })
  }, [currentUser.id]);

  return (
    <div>
      <div className='reading-list-header'>
        <span className='reading-list-title'>Reading list ({bookmarks.length})</span>
        <input
          className='reading-list-search-input'
          type='text'
          placeholder='Search...'
          onChange={(evt) => setSearchTerm(evt.target.value)}/>
      </div>

      <List
        itemLayout='horizontal'
        dataSource={bookmarks.filter(ele => ele.title.toLowerCase().includes(searchTerm.toLowerCase()) || ele.author.toLowerCase().includes(searchTerm.toLowerCase()))}
        renderItem={item => (
          <Bookmark
            bookmark={item}
            bookmarks={bookmarks}
            setBookmarks={setBookmarks}
          />
        )}
        pagination={{
          pageSize: 6,
          style: {
            textAlign: 'center',
            marginTop: '0.125rem !important',
          }
        }}
      >
      </List>
    </div>
  );
}

export default BookmarkList;
